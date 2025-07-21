package com.buy_anytime.product_service.controller;

import lombok.RequiredArgsConstructor;
import com.buy_anytime.common_lib.dto.ApiResponse;
import com.buy_anytime.product_service.dto.attribute.UpdateAttributeRequestDto;
import com.buy_anytime.product_service.entity.Attribute;
import com.buy_anytime.product_service.service.AttributeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createAttribute(@RequestBody UpdateAttributeRequestDto requestDto) {
        Attribute createdAttribute = attributeService.createAttribute(requestDto.getName(), requestDto.getDataType());
        return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), createdAttribute, HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateAttribute(@PathVariable Long id, @RequestBody UpdateAttributeRequestDto requestDto) {
        Attribute updatedAttribute = attributeService.updateAttribute(id, requestDto.getName(), requestDto.getDataType());
        return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), updatedAttribute, HttpStatus.OK.value()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllAttributes() {
        List<Attribute> attributes = attributeService.getAllAttributes();
        return new ResponseEntity<>(new ApiResponse<>(LocalDateTime.now(), attributes, HttpStatus.OK.value()), HttpStatus.OK);
    }
}