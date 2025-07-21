package com.buy_anytime.product_service.dto.product_variant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * This annotation combines @Getter, @Setter, @ToString, @EqualsAndHashCode, and @RequiredArgsConstructor.
 * All the manual getter and setter methods have been removed to avoid conflicts.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductVariantRequestDto {
	private Map<String, String> attributes;
	private BigDecimal price;
	private String sku;
	private Integer initialStock;
	private Integer reorderLevel;
}