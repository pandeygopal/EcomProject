package com.buy_anytime.product_service.controller;

import lombok.RequiredArgsConstructor;
import com.buy_anytime.common_lib.dto.ApiResponse;
import com.buy_anytime.product_service.dto.product_variant.CreateProductVariantRequestDto;
import com.buy_anytime.product_service.dto.product_variant.ProductVariantResponseDto;
import com.buy_anytime.product_service.dto.product_variant.UpdateProductVariantRequestDto;
import com.buy_anytime.product_service.entity.ProductVariant;
import com.buy_anytime.product_service.service.ProductVariantService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;
    private final ModelMapper modelMapper;

    @PostMapping("{productId}")
    public ResponseEntity<ProductVariantResponseDto> createVariant(
            @PathVariable String productId,
            @RequestBody CreateProductVariantRequestDto requestDto) {
        ProductVariantResponseDto variant = productVariantService.createProductVariant(productId, requestDto.getAttributes(), requestDto.getPrice(), requestDto.getSku(), requestDto.getInitialStock(), requestDto.getReorderLevel());
        return ResponseEntity.ok(variant);
    }

    @GetMapping("{productId}")
    public ResponseEntity<List<ProductVariantResponseDto>> getVariants(@PathVariable String productId) {
        List<ProductVariantResponseDto> variants = productVariantService.getVariantsByProductId(productId);
        return ResponseEntity.ok(variants);
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantResponseDto>> getVariantsByIds(@RequestParam("variantIds") Set<Long> variantIds) {
        List<ProductVariant> variants = productVariantService.getProductVariantByIds(variantIds);
        return ResponseEntity.ok(variants.stream().map(productVariant -> modelMapper.map(productVariant, ProductVariantResponseDto.class)).collect(Collectors.toList()));
    }

    @PutMapping("{variantId}")
    public ResponseEntity<ApiResponse<?>> updateVariant(@PathVariable Long variantId, @RequestBody UpdateProductVariantRequestDto requestDto) {
        try {
            ProductVariantResponseDto responseDto = productVariantService.updateProductVariant(variantId, requestDto);
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), responseDto, HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{variantId}")
    public ResponseEntity<ApiResponse<?>> deleteVariant(@PathVariable Long variantId) {
        try {
            productVariantService.deleteProductVariant(variantId);
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), "Variant deleted successfully", HttpStatus.OK.value()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
