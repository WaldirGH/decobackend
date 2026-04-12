package com.deco.apideco.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.deco.apideco.model.Cliente;
import com.deco.apideco.repo.ClienteRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ClienteRepository clienteRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String googleId = oAuth2User.getAttribute("sub");
        String nombre = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String foto = oAuth2User.getAttribute("picture");

        // Guardar cliente si es la primera vez que se loguea
        clienteRepository.findByGoogleId(googleId).orElseGet(() -> {
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setGoogleId(googleId);
            nuevoCliente.setNombre(nombre);
            nuevoCliente.setEmail(email);
            nuevoCliente.setFotoPerfil(foto);
            return clienteRepository.save(nuevoCliente);
        });

        // Redirigir al frontend con el googleId para que haga seguimiento
        response.sendRedirect(
                "https://webdecoballoons.netlify.app/login?googleId=" +
                        URLEncoder.encode(googleId, StandardCharsets.UTF_8));
    }
}