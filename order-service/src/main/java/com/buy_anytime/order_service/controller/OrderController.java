package com.buy_anytime.order_service.controller;
import com.razorpay.Utils;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import com.buy_anytime.common_lib.dto.ApiResponse;
import com.buy_anytime.common_lib.dto.order.OrderDTO;
import com.buy_anytime.order_service.dto.OrderRequestDto;
import com.buy_anytime.order_service.dto.OrderResponseDto;
import com.buy_anytime.order_service.dto.UserDto;
import com.buy_anytime.order_service.exception.OrderException;
import com.buy_anytime.order_service.razorpay.RazorpayService;
import com.buy_anytime.order_service.service.AuthenticationAPIClient;
import com.buy_anytime.order_service.service.OrderService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final AuthenticationAPIClient authenticationAPIClient;
    private final RazorpayService razorpayService;

    @Value("${razorpay.api.secret}")
    private String razorpayApiSecret;

    public OrderController(OrderService orderService, AuthenticationAPIClient authenticationAPIClient, RazorpayService razorpayService) {
        this.orderService = orderService;
        this.authenticationAPIClient = authenticationAPIClient;
        this.razorpayService = razorpayService;
    }

    @PostMapping("/create-payment-order")
    public ResponseEntity<ApiResponse<String>> createPaymentOrder(@RequestBody OrderRequestDto orderRequest) {
        try {
            BigDecimal totalAmount = orderService.calculateTotalAmount(orderRequest);
            String razorpayOrderId = razorpayService.createRazorpayOrder(totalAmount);
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), razorpayOrderId, HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> placeOrder(@RequestBody OrderRequestDto order, HttpServletRequest request) {
        try {
            if ("Razorpay".equalsIgnoreCase(order.getPaymentMethod())) {
                String razorpayOrderId = order.getRazorpayOrderId();
                String razorpayPaymentId = order.getRazorpayPaymentId();
                String razorpaySignature = order.getRazorpaySignature();

                JSONObject options = new JSONObject();
                options.put("razorpay_order_id", razorpayOrderId);
                options.put("razorpay_payment_id", razorpayPaymentId);
                options.put("razorpay_signature", razorpaySignature);

                boolean isSignatureValid = Utils.verifyPaymentSignature(options, razorpayApiSecret);

                if (!isSignatureValid) {
                    throw new OrderException("Invalid Razorpay signature. Payment verification failed.", HttpStatus.BAD_REQUEST);
                }
            }

            String cookie = request.getHeader(HttpHeaders.COOKIE);
            ApiResponse<UserDto> user = authenticationAPIClient.getCurrentUser(cookie).getBody();

            if (user != null && user.getData() != null) {
                OrderDTO response = orderService.placeOrder(order, user.getData().getId(), user.getData().getEmail());
                return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), response, HttpStatus.CREATED.value()), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), null, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);

        } catch (OrderException e) {
            // CORRECTED: The data field must be null to match the OrderDTO type
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), null, e.getStatus().value()), e.getStatus());
        } catch (Exception e) {
            // CORRECTED: The data field must be null to match the OrderDTO type
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> cancelOrder(@PathVariable("orderId") String orderId, HttpServletRequest request) {
        try {
            String cookie = request.getHeader(HttpHeaders.COOKIE);
            ApiResponse<UserDto> user = authenticationAPIClient.getCurrentUser(cookie).getBody();

            if (user != null && user.getData() != null) {
                OrderDTO existingOrder = orderService.cancelOrder(orderId, user.getData().getId());
                return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), existingOrder, HttpStatus.OK.value()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), null, HttpStatus.UNAUTHORIZED.value()), HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            // CORRECTED: The data field must be null to match the OrderDTO type
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderStatus(@PathVariable("orderId") String orderId) {
        try {
            OrderResponseDto existingOrder = orderService.checkOrderStatusByOrderId(orderId);
            if (existingOrder != null) {
                return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), existingOrder, HttpStatus.OK.value()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), null, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), null, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getOrders(HttpServletRequest request,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        try {
            String cookie = request.getHeader(HttpHeaders.COOKIE);
            ApiResponse<UserDto> user = authenticationAPIClient.getCurrentUser(cookie).getBody();
            Object orders = orderService.getAllOrders(user.getData().getId(), page, size);
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), orders, HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update/status/{orderId}")
    public ResponseEntity<ApiResponse<String>> updateOrderStatus(@PathVariable("orderId") String orderId,
                                                                 @RequestHeader(HttpHeaders.IF_MATCH) int version) {
        try {
            OrderResponseDto orderDTO = orderService.updateOrderStatus(orderId, version);
            if (orderDTO != null) {
                return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), "Order status updated", HttpStatus.OK.value()), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), "Order not found", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
        } catch (OptimisticLockException e) {
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}