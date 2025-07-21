package com.buy_anytime.order_service.dto.attribute_value;

import com.buy_anytime.order_service.dto.attribute.AttributeResponseDto;
public class AttributeValueResponseDto {
    private AttributeResponseDto attribute;
    private String value;
	public AttributeValueResponseDto(AttributeResponseDto attribute, String value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}
    
    
}
