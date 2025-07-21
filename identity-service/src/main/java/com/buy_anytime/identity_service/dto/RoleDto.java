package com.buy_anytime.identity_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.buy_anytime.identity_service.enums.ERole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDto {
    private ERole authority;
    
}