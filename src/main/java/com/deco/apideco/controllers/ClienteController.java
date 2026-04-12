package com.deco.apideco.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deco.apideco.DTO.response.ClienteResponse;
import com.deco.apideco.services.ClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/perfil")
    public ResponseEntity<ClienteResponse> obtenerPerfil(
            @AuthenticationPrincipal OAuth2User principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String googleId = principal.getAttribute("sub");
        return ResponseEntity.ok(clienteService.obtenerPerfilPorGoogleId(googleId));
    }
}
