package com.buy_anytime.order_service.service;

import com.buy_anytime.common_lib.dto.ApiResponse;

import com.buy_anytime.order_service.dto.product.ProductResponseDto;
import com.buy_anytime.order_service.dto.product_variant.ProductVariantResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductAPIClient {
    @GetMapping("/api/v1/products/products")
    ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductsByIds(@RequestParam("ids") Set<String> productIds);

    @GetMapping("/api/v1/products/variants")
    ResponseEntity<List<ProductVariantResponseDto>> getProductsByVariantIds(@RequestParam("variantIds") Set<Long> variantIds);
}
