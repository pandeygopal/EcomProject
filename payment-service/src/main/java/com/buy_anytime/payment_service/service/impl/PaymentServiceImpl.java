package com.buy_anytime.payment_service.service.impl;

import lombok.RequiredArgsConstructor;
import com.buy_anytime.common_lib.dto.order.OrderEvent;
import com.buy_anytime.common_lib.dto.order.OrderItemDTO;
import com.buy_anytime.payment_service.dto.PaymentDto;
import com.buy_anytime.payment_service.entity.Payment;
import com.buy_anytime.payment_service.entity.PaymentStatus;
import com.buy_anytime.payment_service.redis.PaymentRedis;
import com.buy_anytime.payment_service.repository.PaymentRepository;
import com.buy_anytime.payment_service.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final PaymentRedis paymentRedis;

    @Override
    public void createPayment(OrderEvent orderEvent) {
        Payment newPayment = new Payment();
        newPayment.setId(UUID.randomUUID().toString());

        BigDecimal amount = BigDecimal.ZERO;

        for (OrderItemDTO orderItemDTO : orderEvent.getOrderDTO().getOrderItems()) {
            BigDecimal itemTotal = orderItemDTO.getPrice().multiply(BigDecimal.valueOf(orderItemDTO.getQuantity()));
            amount = amount.add(itemTotal);
        }

        newPayment.setAmount(amount);
        newPayment.setPaymentMethod(orderEvent.getPaymentMethod());
        newPayment.setOrderId(orderEvent.getOrderDTO().getOrderId());
        newPayment.setStatus(PaymentStatus.PENDING);
        paymentRedis.save(newPayment);
        paymentRepository.save(newPayment);
    }

    @Override
    public PaymentDto getPaymentByOrderId(String orderId) {
        Payment cachePayment = paymentRedis.findByOrderId(orderId);

        if(cachePayment != null){
            return modelMapper.map(cachePayment, PaymentDto.class);
        }

        Payment existingPayment = paymentRepository.findByOrderId(orderId);
        if(existingPayment != null){
            paymentRedis.save(existingPayment);
            return modelMapper.map(existingPayment, PaymentDto.class);
        }
        return null;
    }

    @Override
    public void updateStatusPayment(String orderId, PaymentStatus status) {
        Payment existingPayment = paymentRepository.findByOrderId(orderId);
        if(existingPayment != null){
            existingPayment.setStatus(status);
            paymentRedis.save(existingPayment);
            paymentRepository.save(existingPayment);
        } else {
            LOGGER.warn("Payment not found!");
        }
    }

    @Override
    public void refundPayment(String orderId) {
        Payment existingPayment = paymentRepository.findByOrderId(orderId);
        if(existingPayment != null){
            existingPayment.setStatus(PaymentStatus.REFUND);
            paymentRepository.save(existingPayment);
        } else {
            LOGGER.warn("Payment not found!");
        }
    }
}