package com.buy_anytime.order_service.service.state;

import com.buy_anytime.order_service.entity.Order;

public class DeliveredOrderState implements OrderState {
    @Override
    public void handleStateChange(Order order) {
        throw new IllegalStateException("Order has already been delivered and cannot be updated.");
    }
}

