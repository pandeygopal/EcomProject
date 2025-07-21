package com.buy_anytime.product_service.dto.attribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAttributeRequestDto {
    private String name;
    private String dataType;
}
