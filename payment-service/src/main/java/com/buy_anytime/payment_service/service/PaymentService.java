package com.buy_anytime.payment_service.service;


import com.buy_anytime.common_lib.dto.order.OrderEvent;
import com.buy_anytime.payment_service.dto.PaymentDto;
import com.buy_anytime.payment_service.entity.PaymentStatus;

public interface PaymentService {
    void createPayment(OrderEvent orderEvent);
    PaymentDto getPaymentByOrderId(String orderId);
    void updateStatusPayment(String orderId, PaymentStatus status);
    void refundPayment(String orderId);
}
