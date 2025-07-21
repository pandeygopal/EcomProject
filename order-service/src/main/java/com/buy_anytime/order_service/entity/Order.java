package com.buy_anytime.order_service.entity;

import jakarta.persistence.*;
import com.buy_anytime.common_lib.entity.AbstractEntity;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends AbstractEntity {
    @Id
    private String orderId;

    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private Long userId;
    private int version;
    
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Order(String orderId, String status, List<OrderItem> orderItems, Long userId, int version) {
		this.orderId = orderId;
		this.status = status;
		this.orderItems = orderItems;
		this.userId = userId;
		this.version = version;
	}
    
    
}


