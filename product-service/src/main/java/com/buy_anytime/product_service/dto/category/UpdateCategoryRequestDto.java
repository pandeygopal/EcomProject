package com.buy_anytime.product_service.dto.category;

import lombok.Data;

@Data
public class UpdateCategoryRequestDto {
    private String id;
    private String name;
    private String parentId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public UpdateCategoryRequestDto(String id, String name, String parentId) {
		super();
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}
	public UpdateCategoryRequestDto() {
	}
    
}