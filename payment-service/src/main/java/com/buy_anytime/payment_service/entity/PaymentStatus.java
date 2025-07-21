package com.buy_anytime.payment_service.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    PENDING("Pending"),
    SUCCESS("Success"),
    FAILED("Failed"),
    REFUND("Refund");

    public final String label;

	public String getLabel() {
		return label;
	}
    
    
    
}
