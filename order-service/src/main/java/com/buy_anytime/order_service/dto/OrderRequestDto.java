package com.buy_anytime.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.buy_anytime.common_lib.dto.order.OrderItemDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
	private String orderId;
	private String status;
	private List<OrderItemDTO> orderItems;
	private String paymentMethod;

	// ADDED: Fields for Razorpay verification
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorpaySignature;
}
