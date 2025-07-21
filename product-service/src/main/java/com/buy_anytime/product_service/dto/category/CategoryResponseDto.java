package com.buy_anytime.product_service.dto.category;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryResponseDto {
    private String id;
    private String name;
    private String parentId;
    private List<CategoryResponseDto> children = new ArrayList<>();
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
	public List<CategoryResponseDto> getChildren() {
		return children;
	}
	public void setChildren(List<CategoryResponseDto> children) {
		this.children = children;
	}
	public CategoryResponseDto(String id, String name, String parentId, List<CategoryResponseDto> children) {
		super();
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.children = children;
	}
	public CategoryResponseDto() {
	}
    
}