package com.buy_anytime.product_service.repository;

import com.buy_anytime.product_service.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    List<AttributeValue> findByProductVariantId(Long productVariantId);
}
