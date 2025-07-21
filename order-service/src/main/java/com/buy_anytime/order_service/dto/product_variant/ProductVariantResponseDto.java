package com.buy_anytime.order_service.dto.product_variant;
import com.buy_anytime.order_service.dto.attribute_value.AttributeValueResponseDto;

import java.math.BigDecimal;
import java.util.Set;
public class ProductVariantResponseDto {
    private Long id;
    private Set<AttributeValueResponseDto> attributeValues;
    private BigDecimal price;
    private String sku;
    private Integer stockQuantity;
    private Integer reorderLevel;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<AttributeValueResponseDto> getAttributeValues() {
		return attributeValues;
	}
	public void setAttributeValues(Set<AttributeValueResponseDto> attributeValues) {
		this.attributeValues = attributeValues;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
	public Integer getReorderLevel() {
		return reorderLevel;
	}
	public void setReorderLevel(Integer reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
	public ProductVariantResponseDto(Long id, Set<AttributeValueResponseDto> attributeValues, BigDecimal price,
			String sku, Integer stockQuantity, Integer reorderLevel) {
		super();
		this.id = id;
		this.attributeValues = attributeValues;
		this.price = price;
		this.sku = sku;
		this.stockQuantity = stockQuantity;
		this.reorderLevel = reorderLevel;
	}
    
    
}
