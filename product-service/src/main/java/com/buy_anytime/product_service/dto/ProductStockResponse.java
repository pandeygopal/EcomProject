package com.buy_anytime.product_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.buy_anytime.product_service.dto.product.ProductResponseDto;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductStockResponse {
    private ProductResponseDto product;

	public ProductResponseDto getProduct() {
		return product;
	}

	public void setProduct(ProductResponseDto product) {
		this.product = product;
	}
    
}
