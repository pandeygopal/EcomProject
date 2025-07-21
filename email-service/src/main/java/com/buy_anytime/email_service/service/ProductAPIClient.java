package com.buy_anytime.email_service.service;

import com.buy_anytime.common_lib.dto.ApiResponse;
import com.buy_anytime.email_service.dto.ProductStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductAPIClient {
    @GetMapping("api/v1/products/{id}")
    ResponseEntity<ApiResponse<ProductStockResponse>> getProductById(@RequestParam("id") String id);
}
