package com.buy_anytime.product_service.dto.category;

import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    private String name;
    private String parentId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public CreateCategoryRequestDto(String name, String parentId) {
		super();
		this.name = name;
		this.parentId = parentId;
	} 
	public CreateCategoryRequestDto() {
	} 
    
}