package com.buy_anytime.product_service.controller;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.buy_anytime.common_lib.dto.ApiResponse;
import com.buy_anytime.product_service.dto.product.CreateProductRequestDto;
import com.buy_anytime.product_service.dto.product.ProductResponseDto;
import com.buy_anytime.product_service.dto.product.UpdateProductRequestDto;
import com.buy_anytime.product_service.exception.ProductException;
import com.buy_anytime.product_service.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> saveProduct(@ModelAttribute @Valid CreateProductRequestDto createProductRequestDto) {
        try {
            ProductResponseDto createdProductDto = productService.saveProduct(createProductRequestDto);
            ApiResponse<ProductResponseDto> apiResponse = new ApiResponse<>(LocalDateTime.now(), createdProductDto, HttpStatus.CREATED.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getProductList(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ProductResponseDto> productList = productService.getProductList(page, size);
            ApiResponse<Page<ProductResponseDto>> apiResponse = new ApiResponse<>(LocalDateTime.now(), productList, HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable("id") String id) {
        try {
            ProductResponseDto productStockResponse = productService.getProductById(id);
            ApiResponse<ProductResponseDto> apiResponse = new ApiResponse<>(LocalDateTime.now(), productStockResponse, HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (ProductException e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), e.getStatus().value());
            return new ResponseEntity<>(response, e.getStatus());
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateProduct(@PathVariable("id") String id, @RequestBody UpdateProductRequestDto productDTO, @RequestHeader(HttpHeaders.IF_MATCH) int version) {
        try {
            ProductResponseDto productStockResponse = productService.updateProduct(id, productDTO, version);
            ApiResponse<ProductResponseDto> apiResponse = new ApiResponse<>(LocalDateTime.now(), productStockResponse, HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (OptimisticLockException e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.CONFLICT.value());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable("id") String id) {
        try {
            productService.deleteProduct(id);
            // Return a success response with no content
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), "Product deleted successfully", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (ProductException e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), e.getStatus().value());
            return new ResponseEntity<>(response, e.getStatus());
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable) {
        Page<ProductResponseDto> products = productService.searchProducts(name, categoryId, minPrice, maxPrice, pageable);
        ApiResponse<Page<ProductResponseDto>> response = new ApiResponse<>(LocalDateTime.now(), products, HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<?>> getProductsByIds(@RequestParam("ids") Set<String> productIds) {
        try {
            List<ProductResponseDto> productDTOs = productService.getProductsByIds(productIds);
            ApiResponse<List<ProductResponseDto>> apiResponse = new ApiResponse<>(LocalDateTime.now(), productDTOs, HttpStatus.OK.value());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
