package com.buy_anytime.order_service.service.state;

import com.buy_anytime.order_service.entity.Order;

public interface OrderState {
    void handleStateChange(Order order);
}
