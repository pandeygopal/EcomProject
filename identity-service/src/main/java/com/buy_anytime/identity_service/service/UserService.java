package com.buy_anytime.identity_service.service;

import com.buy_anytime.identity_service.dto.UserDto;

public interface UserService {
    UserDto getUserByUsername(String username);
}
