package com.buy_anytime.order_service.dto.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AttributeResponseDto {
    private Long id;
    private String name;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AttributeResponseDto(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
    
    
}
