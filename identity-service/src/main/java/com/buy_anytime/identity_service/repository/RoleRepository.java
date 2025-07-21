package com.buy_anytime.identity_service.repository;

import com.buy_anytime.identity_service.entity.Role;
import com.buy_anytime.identity_service.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}