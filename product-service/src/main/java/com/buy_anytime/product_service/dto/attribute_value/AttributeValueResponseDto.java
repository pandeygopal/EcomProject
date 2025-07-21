package com.buy_anytime.product_service.dto.attribute_value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.buy_anytime.product_service.dto.attribute.AttributeResponseDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValueResponseDto {
    private AttributeResponseDto attribute;
    private String value;
    private String name;
    
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AttributeResponseDto getAttribute() {
		return attribute;
	}
	public void setAttribute(AttributeResponseDto attribute) {
		this.attribute = attribute;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    
}
