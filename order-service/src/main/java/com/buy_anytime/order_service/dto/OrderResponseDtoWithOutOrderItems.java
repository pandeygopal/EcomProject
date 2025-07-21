package com.buy_anytime.order_service.dto;

public class OrderResponseDtoWithOutOrderItems {
    private OrderWithOutOrderItems orderDTO;
    private PaymentDto paymentDto;
	public OrderWithOutOrderItems getOrderDTO() {
		return orderDTO;
	}
	public void setOrderDTO(OrderWithOutOrderItems orderDTO) {
		this.orderDTO = orderDTO;
	}
	public PaymentDto getPaymentDto() {
		return paymentDto;
	}
	public void setPaymentDto(PaymentDto paymentDto) {
		this.paymentDto = paymentDto;
	}
	public OrderResponseDtoWithOutOrderItems(OrderWithOutOrderItems orderDTO, PaymentDto paymentDto) {
		super();
		this.orderDTO = orderDTO;
		this.paymentDto = paymentDto;
	}
	public OrderResponseDtoWithOutOrderItems() {
	}
	
    
    
    
    
    
}
