package com.buy_anytime.order_service.dto;
import java.math.BigDecimal;
public class PaymentDto {
    private String orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public PaymentDto(String orderId, BigDecimal amount, String paymentMethod, String status) {
		this.orderId = orderId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.status = status;
	}
    
    
    
}
