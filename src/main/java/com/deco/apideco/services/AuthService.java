package com.deco.apideco.services;

import com.deco.apideco.DTO.request.LoginRequest;
import com.deco.apideco.DTO.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
