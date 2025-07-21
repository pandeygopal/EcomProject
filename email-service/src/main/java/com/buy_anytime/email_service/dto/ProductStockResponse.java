package com.buy_anytime.email_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductStockResponse {
    private ProductResponseDto product;
    private StockResponseDto stock;
	public ProductResponseDto getProduct() {
		return product;
	}
	public void setProduct(ProductResponseDto product) {
		this.product = product;
	}
	public StockResponseDto getStock() {
		return stock;
	}
	public void setStock(StockResponseDto stock) {
		this.stock = stock;
	}
	
    
    
}
