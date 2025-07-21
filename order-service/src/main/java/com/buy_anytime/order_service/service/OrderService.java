package com.buy_anytime.order_service.service;

import com.buy_anytime.common_lib.dto.order.OrderDTO;
import com.buy_anytime.order_service.dto.OrderRequestDto;
import com.buy_anytime.order_service.dto.OrderResponseDto;
import com.buy_anytime.order_service.dto.OrderResponseDtoWithOutOrderItems;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    OrderDTO placeOrder(OrderRequestDto order, Long userId, String email);
    OrderResponseDto checkOrderStatusByOrderId(String orderId);
    OrderResponseDto updateOrderStatus(String orderId, int version);
    OrderDTO cancelOrder(String orderId, Long userId);
    List<OrderResponseDtoWithOutOrderItems> getAllOrders(Long userId, int page, int size);

    // ADDED: New method to calculate total amount
    BigDecimal calculateTotalAmount(OrderRequestDto orderRequestDto);
}
