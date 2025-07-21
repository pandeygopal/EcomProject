package com.buy_anytime.order_service.entity;

public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    SHIPPING("Shipping"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    public String label;

	public String getLabel() {
		return label;
	}
	private OrderStatus(String label) {
		this.label = label;
	}
    
    
}
