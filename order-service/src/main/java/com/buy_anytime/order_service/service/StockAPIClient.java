package com.buy_anytime.order_service.service;

import com.buy_anytime.order_service.dto.StockDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "STOCK-SERVICE")
public interface StockAPIClient {
    @GetMapping("api/v1/stock")
    ResponseEntity<List<StockDto>> getProductsStock(@RequestParam("productIds") Set<String> productIds);
}
