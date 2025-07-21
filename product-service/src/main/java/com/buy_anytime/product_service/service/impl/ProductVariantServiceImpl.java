package com.buy_anytime.product_service.service.impl;

import lombok.RequiredArgsConstructor;
import com.buy_anytime.product_service.dto.attribute_value.AttributeValueResponseDto;
import com.buy_anytime.product_service.dto.product_variant.ProductVariantResponseDto;
import com.buy_anytime.product_service.dto.product_variant.UpdateProductVariantRequestDto;
import com.buy_anytime.product_service.entity.Attribute;
import com.buy_anytime.product_service.entity.AttributeValue;
import com.buy_anytime.product_service.entity.Product;
import com.buy_anytime.product_service.entity.ProductVariant;
import com.buy_anytime.product_service.exception.ProductException;
import com.buy_anytime.product_service.redis.ProductRedis;
import com.buy_anytime.product_service.repository.AttributeRepository;
import com.buy_anytime.product_service.repository.ProductRepository;
import com.buy_anytime.product_service.repository.ProductVariantRepository;
import com.buy_anytime.product_service.service.ProductVariantService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantRepository productVariantRepository;

    private final AttributeRepository attributeRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    private final ProductRedis productRedis;

	@Transactional
    @Override
    public ProductVariantResponseDto createProductVariant(String productId, Map<String, String> attributes, BigDecimal price, String sku, Integer initialStock, Integer reorderLevel) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setPrice(price);
        variant.setSku(sku);
        variant.setStockQuantity(initialStock);
        variant.setReorderLevel(reorderLevel);
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            Attribute attribute = attributeRepository.findByName(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("Attribute " + entry.getKey() + " not found."));
            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setProductVariant(variant);
            attributeValue.setAttribute(attribute);
            attributeValue.setValue(entry.getValue());
            variant.getAttributeValues().add(attributeValue);
        }

        ProductVariant savedVariant = productVariantRepository.save(variant);

        product.getVariants().add(savedVariant);
        productRedis.save(product);

        return modelMapper.map(savedVariant, ProductVariantResponseDto.class);
    }

    @Override
    public List<ProductVariantResponseDto> getVariantsByProductId(String productId) {
        return productVariantRepository.findByProductId(productId)
                .stream()
                .map(productVariant -> modelMapper.map(productVariant, ProductVariantResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProductVariantResponseDto updateProductVariant(Long variantId, UpdateProductVariantRequestDto updateDTO) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ProductException("ProductVariant not found", HttpStatus.NOT_FOUND));
        if (updateDTO.getPrice() != null) {
            variant.setPrice(updateDTO.getPrice());
        }
        if (updateDTO.getSku() != null) {
            variant.setSku(updateDTO.getSku());
        }
        if (updateDTO.getStockQuantity() != null) {
            variant.setStockQuantity(updateDTO.getStockQuantity());
        }
        if (updateDTO.getReorderLevel() != null) {
            variant.setReorderLevel(updateDTO.getReorderLevel());
        }
        if (updateDTO.getAttributeValues() != null && !updateDTO.getAttributeValues().isEmpty()) {
            variant.getAttributeValues().clear();
            for(AttributeValueResponseDto attrDto : updateDTO.getAttributeValues()) {
                Attribute attribute = attributeRepository.findByName(attrDto.getAttribute().getName())
                        .orElseThrow(() -> new IllegalArgumentException("Attribute " + attrDto.getAttribute().getName() + " not found."));
                AttributeValue attributeValue = new AttributeValue();
                attributeValue.setProductVariant(variant);
                attributeValue.setAttribute(attribute);
                attributeValue.setValue(attrDto.getValue());
                variant.getAttributeValues().add(attributeValue);
            }
        }

        ProductVariant updatedVariant = productVariantRepository.save(variant);
       Product productAfterUpdate = variant.getProduct();
        productRedis.save(productAfterUpdate);

        return modelMapper.map(updatedVariant, ProductVariantResponseDto.class);
    }

    /**
     * Xóa một ProductVariant
     */
    @Transactional
    public void deleteProductVariant(Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ProductException("ProductVariant not found", HttpStatus.NOT_FOUND));

        Product product = variant.getProduct();
        productVariantRepository.delete(variant);
//        stockClient.deleteStock(variantId);
        productRedis.save(product);
    }

    @Override
    public void saveProductVariant(ProductVariant productVariant) {
        productVariantRepository.save(productVariant);
    }

    @Override
    public List<ProductVariant> getProductVariantByIds(Set<Long> variantIds) {
        return productVariantRepository.findAllByIdIn(variantIds);
    }

    @Override
    public ProductVariant getVariantById(Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId).orElseThrow(() -> new ProductException("Product variant not found with ID: " + variantId, HttpStatus.NOT_FOUND));
        return variant;
    }
}
