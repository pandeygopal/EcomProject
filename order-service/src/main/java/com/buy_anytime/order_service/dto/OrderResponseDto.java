package com.buy_anytime.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.buy_anytime.common_lib.dto.order.OrderDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private OrderDTO orderDTO;
    private PaymentDto paymentDto;
	public OrderDTO getOrderDTO() {
		return orderDTO;
	}
	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}
	public PaymentDto getPaymentDto() {
		return paymentDto;
	}
	public void setPaymentDto(PaymentDto paymentDto) {
		this.paymentDto = paymentDto;
	}
    
}
