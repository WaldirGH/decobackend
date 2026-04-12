package com.deco.apideco.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.deco.apideco.DTO.request.LoginRequest;
import com.deco.apideco.DTO.response.AuthResponse;
import com.deco.apideco.model.Usuario;
import com.deco.apideco.repo.UsuarioRepository;
import com.deco.apideco.security.JwtService;
import com.deco.apideco.services.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword()))
            throw new RuntimeException("Credenciales incorrectas");

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRol().name());

        return new AuthResponse(token, usuario.getNombre(), usuario.getEmail(), usuario.getRol());
    }
}
