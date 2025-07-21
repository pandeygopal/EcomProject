package com.buy_anytime.order_service.service.state;

import com.buy_anytime.order_service.entity.Order;
import com.buy_anytime.order_service.entity.OrderStatus;

public class NewOrderState implements OrderState{

    @Override
    public void handleStateChange(Order order) {
        order.setStatus(OrderStatus.PROCESSING.getLabel());
    }
}

