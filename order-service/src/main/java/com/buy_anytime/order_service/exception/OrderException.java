package com.buy_anytime.order_service.exception;

import org.springframework.http.HttpStatus;

public class OrderException extends RuntimeException {
    private HttpStatus status;

    public OrderException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

	public OrderException(HttpStatus status) {
		super();
		this.status = status;
	}
    
    
    
}

