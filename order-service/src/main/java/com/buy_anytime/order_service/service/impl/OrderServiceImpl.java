package com.buy_anytime.order_service.service.impl;

import com.buy_anytime.order_service.dto.*;
import jakarta.persistence.OptimisticLockException;
import com.buy_anytime.common_lib.dto.order.OrderDTO;
import com.buy_anytime.common_lib.dto.order.OrderEvent;
import com.buy_anytime.common_lib.dto.order.OrderItemDTO;
import com.buy_anytime.order_service.dto.product.ProductResponseDto;
import com.buy_anytime.order_service.dto.product_variant.ProductVariantResponseDto;
import com.buy_anytime.order_service.entity.Order;
import com.buy_anytime.order_service.entity.OrderItem;
import com.buy_anytime.order_service.entity.OrderStatus;
import com.buy_anytime.order_service.exception.OrderException;
import com.buy_anytime.order_service.kafka.OrderProducer;
import com.buy_anytime.order_service.redis.OrderRedis;
import com.buy_anytime.order_service.repository.OrderRepository;
import com.buy_anytime.order_service.service.OrderService;
import com.buy_anytime.order_service.service.PaymentAPIClient;
import com.buy_anytime.order_service.service.ProductAPIClient;
import com.buy_anytime.order_service.service.state.OrderContext;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderProducer orderProducer;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductAPIClient productAPIClient;
    private final PaymentAPIClient paymentAPIClient;
    private final OrderRedis orderRedis;

    public OrderServiceImpl(OrderProducer orderProducer, OrderRepository orderRepository, ModelMapper modelMapper,
                            ProductAPIClient productAPIClient, PaymentAPIClient paymentAPIClient,
                            OrderRedis orderRedis) {
        this.orderProducer = orderProducer;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productAPIClient = productAPIClient;
        this.paymentAPIClient = paymentAPIClient;
        this.orderRedis = orderRedis;
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(OrderRequestDto orderRequestDto, Long userId, String email) {
        try {
            OrderDTO newOrder = createOrderDTO(orderRequestDto, userId);
            validateStockAndPrice(orderRequestDto, newOrder);

            Order createdOrder = saveOrder(newOrder, orderRequestDto);
            orderRedis.save(modelMapper.map(createdOrder, OrderDTO.class));

            sendOrderEvent(createdOrder, orderRequestDto.getPaymentMethod(), email);

            return modelMapper.map(createdOrder, OrderDTO.class);
        } catch (OrderException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Failed to create order: {}", e.getMessage(), e);
            throw new OrderException("Failed to create order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public BigDecimal calculateTotalAmount(OrderRequestDto orderRequestDto) {
        Set<String> productIds = extractProductIds(orderRequestDto.getOrderItems());
        List<ProductResponseDto> products = fetchProducts(productIds);
        Map<String, ProductResponseDto> productsMap = products.stream()
                .collect(Collectors.toMap(ProductResponseDto::getId, Function.identity()));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO item : orderRequestDto.getOrderItems()) {
            ProductResponseDto product = productsMap.get(item.getProductId());
            if (product != null) {
                ProductVariantResponseDto selectedVariant = findMatchingVariant(product.getVariants(), item.getVariantId());
                BigDecimal itemPrice = (selectedVariant != null && selectedVariant.getPrice() != null) ? selectedVariant.getPrice() : product.getPrice();
                totalAmount = totalAmount.add(itemPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
            } else {
                throw new OrderException("Product not found with id: " + item.getProductId(), HttpStatus.BAD_REQUEST);
            }
        }
        return totalAmount;
    }

    @Override
    public OrderDTO cancelOrder(String orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order not found", HttpStatus.NOT_FOUND));

        // Basic check to see if the user owns the order (can be enhanced with roles)
        if (!order.getUserId().equals(userId)) {
            throw new OrderException("User not authorized to cancel this order", HttpStatus.FORBIDDEN);
        }

        order.setStatus(OrderStatus.CANCELED.getLabel());

        // Note: Razorpay refunds are typically handled via a separate process or their dashboard.
        // For now, we will just update our system's status.
        // A refund event could be published to Kafka here if needed.

        OrderDTO savedOrderDTO = modelMapper.map(orderRepository.save(order), OrderDTO.class);
        orderRedis.save(savedOrderDTO);
        return savedOrderDTO;
    }

    // Unchanged methods (checkOrderStatusByOrderId, updateOrderStatus, getAllOrders, etc.)
    // ...
    @Override
    public OrderResponseDto checkOrderStatusByOrderId(String orderId) {
        OrderDTO cachedOrder = orderRedis.findByOrderId(orderId);
        OrderDTO order;

        if (cachedOrder != null) {
            order = cachedOrder;
            LOGGER.info("Order retrieved from Redis cache.");
        } else {
            order = modelMapper.map(orderRepository.findById(orderId).orElse(null), OrderDTO.class);
            if (order != null) {
                orderRedis.save(order);
                LOGGER.info("Order retrieved from DB and saved to Redis cache.");
            }
        }

        if (order != null) {
            OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
            PaymentDto paymentDto = paymentAPIClient.getPaymentByOrderId(orderId).getBody().getData();

            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setOrderDTO(orderDTO);
            orderResponseDto.setPaymentDto(paymentDto);

            return orderResponseDto;
        }
        return null;
    }


    @Override
    public OrderResponseDto updateOrderStatus(String orderId, int version) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            if (order.getVersion() != version) {
                throw new OptimisticLockException("Version conflict!");
            }

            OrderContext context = new OrderContext(order);
            context.handleStateChange(order);

            Order savedOrder = orderRepository.save(order);
            OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
            orderRedis.save(orderDTO);

            PaymentDto paymentDto = paymentAPIClient.getPaymentByOrderId(orderId).getBody().getData();

            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setOrderDTO(orderDTO);
            orderResponseDto.setPaymentDto(paymentDto);
            return orderResponseDto;
        }
        return null;
    }

    @Override
    public List<OrderResponseDtoWithOutOrderItems> getAllOrders(Long userId, int page, int size) {
        Page<Order> orderPage = orderRepository.findByUserId(userId, PageRequest.of(page, size));
        return orderPage.stream().map(order -> {
            OrderDTO cachedOrder = orderRedis.findByOrderId(order.getOrderId());
            OrderWithOutOrderItems orderDTO = modelMapper.map(order, OrderWithOutOrderItems.class);
            if (cachedOrder == null) {
                orderRedis.save(modelMapper.map(order, OrderDTO.class));
            }
            PaymentDto paymentDto = paymentAPIClient.getPaymentByOrderId(order.getOrderId()).getBody().getData();
            return new OrderResponseDtoWithOutOrderItems(orderDTO, paymentDto);
        }).collect(Collectors.toList());
    }

    // --- Private Helper Methods ---

    private void validateStockAndPrice(OrderRequestDto orderRequestDTO, OrderDTO newOrder) {
        Set<String> productIds = extractProductIds(orderRequestDTO.getOrderItems());
        List<ProductResponseDto> products = fetchProducts(productIds);

        if (products.size() != productIds.size()) {
            throw new OrderException("One or more products not found for given IDs.", HttpStatus.BAD_REQUEST);
        }

        Map<String, ProductResponseDto> productsMap = products.stream()
                .collect(Collectors.toMap(ProductResponseDto::getId, Function.identity()));

        for (OrderItemDTO orderItem : orderRequestDTO.getOrderItems()) {
            ProductResponseDto product = productsMap.get(orderItem.getProductId());
            if (product == null) {
                throw new OrderException("Not found product with ID: " + orderItem.getProductId(), HttpStatus.NOT_FOUND);
            }
            updatePriceAndValidateStock(orderItem, product);
        }
    }

    private Order saveOrder(OrderDTO newOrder, OrderRequestDto orderRequestDto) {
        Order order = modelMapper.map(newOrder, Order.class);

        if ("Razorpay".equalsIgnoreCase(orderRequestDto.getPaymentMethod())) {
            // The Razorpay Order ID is used for client-side checkout, not our internal ID.
            // We generate our own unique ID.
            order.setOrderId(UUID.randomUUID().toString());
        } else { // For COD or other methods
            order.setOrderId(UUID.randomUUID().toString());
        }

        order.setStatus(OrderStatus.PENDING.getLabel());
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrder(order);
        }
        return orderRepository.save(order);
    }

    // Other private methods remain the same
    private Set<String> extractProductIds(List<OrderItemDTO> orderItems) {
        return orderItems.stream().map(OrderItemDTO::getProductId).collect(Collectors.toSet());
    }

    private List<ProductResponseDto> fetchProducts(Set<String> productIds) {
        return productAPIClient.getProductsByIds(productIds).getBody().getData();
    }

    private ProductVariantResponseDto findMatchingVariant(List<ProductVariantResponseDto> variants, Long variantId) {
        if (variants == null || variantId == null) return null;
        return variants.stream().filter(v -> v.getId().equals(variantId)).findFirst().orElse(null);
    }

    private void updatePriceAndValidateStock(OrderItemDTO orderItem, ProductResponseDto product) {
        ProductVariantResponseDto selectedVariant = findMatchingVariant(product.getVariants(), orderItem.getVariantId());
        if (selectedVariant != null) {
            if (selectedVariant.getStockQuantity() < orderItem.getQuantity()) {
                throw new OrderException("Insufficient stock for variantId: " + selectedVariant.getId(), HttpStatus.BAD_REQUEST);
            }
            orderItem.setPrice(selectedVariant.getPrice() != null ? selectedVariant.getPrice() : product.getPrice());
        } else {
            throw new OrderException("Variant with ID " + orderItem.getVariantId() + " not found for product " + product.getId(), HttpStatus.BAD_REQUEST);
        }
    }

    private OrderDTO createOrderDTO(OrderRequestDto orderRequestDto, Long userId) {
        OrderDTO newOrder = modelMapper.map(orderRequestDto, OrderDTO.class);
        newOrder.setUserId(userId);
        return newOrder;
    }

    private void sendOrderEvent(Order createdOrder, String paymentMethod, String email) {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderDTO(modelMapper.map(createdOrder, OrderDTO.class));
        orderEvent.setStatus("PENDING");
        orderEvent.setMessage("Order status is in pending state");
        orderEvent.setPaymentMethod(paymentMethod);
        orderEvent.setEmail(email);
        orderProducer.sendMessage(orderEvent);
    }
}
