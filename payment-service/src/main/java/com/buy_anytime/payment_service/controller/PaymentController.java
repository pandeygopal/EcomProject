package com.buy_anytime.payment_service.controller;

import lombok.RequiredArgsConstructor;
import com.buy_anytime.common_lib.dto.ApiResponse;
import com.buy_anytime.payment_service.dto.PaymentDto;
import com.buy_anytime.payment_service.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime; // Ensure this import is present

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("{orderId}")
    public ResponseEntity<ApiResponse<?>> getPaymentByOrderId(@PathVariable("orderId") String orderId) {
        try {
            PaymentDto payment = paymentService.getPaymentByOrderId(orderId);
            return payment != null
                    // Corrected: Added LocalDateTime.now()
                    ? ResponseEntity.ok(new ApiResponse<>(LocalDateTime.now(), payment, HttpStatus.OK.value()))
                    : ResponseEntity.status(HttpStatus.NOT_FOUND)
                    // Corrected: Added LocalDateTime.now()
                    .body(new ApiResponse<>(LocalDateTime.now(), "Unknown order ID: " + orderId, HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    // Corrected: Added LocalDateTime.now()
                    .body(new ApiResponse<>(
                                    LocalDateTime.now(),
                                    e.getMessage(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value()
                            )
                    );
        }
    }
}
