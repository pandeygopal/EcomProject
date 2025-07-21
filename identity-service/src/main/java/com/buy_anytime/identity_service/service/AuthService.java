package com.buy_anytime.identity_service.service;

import jakarta.servlet.http.HttpServletResponse;
import com.buy_anytime.identity_service.dto.AuthRequest;
import com.buy_anytime.identity_service.dto.SignUpRequest;

public interface AuthService {
    String saveUser(SignUpRequest userCredential);
    String generateToken(AuthRequest authRequest, HttpServletResponse response);
    void validateToken(String token);
}
