package com.buy_anytime.common_lib.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductEvent {
    private ProductDTO productDTO;
    private ProductMethod method;
	public ProductDTO getProductDTO() {
		return productDTO;
	}
	public void setProductDTO(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}
	public ProductMethod getMethod() {
		return method;
	}
	public void setMethod(ProductMethod method) {
		this.method = method;
	}
    
}
