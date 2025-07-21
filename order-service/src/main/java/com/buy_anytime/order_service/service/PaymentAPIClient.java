package com.buy_anytime.order_service.service;

import com.buy_anytime.common_lib.dto.ApiResponse;
import com.buy_anytime.order_service.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentAPIClient {
    @GetMapping("api/v1/payment/{orderId}")
    ResponseEntity<ApiResponse<PaymentDto>> getPaymentByOrderId(@PathVariable("orderId") String orderId);
}
