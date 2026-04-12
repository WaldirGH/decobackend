package com.deco.apideco.services.impl;

import org.springframework.stereotype.Service;

import com.deco.apideco.DTO.response.ClienteResponse;
import com.deco.apideco.model.Cliente;
import com.deco.apideco.repo.ClienteRepository;
import com.deco.apideco.services.ClienteService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponse obtenerPerfilPorGoogleId(String googleId) {
        Cliente cliente = clienteRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setNombre(cliente.getNombre());
        response.setEmail(cliente.getEmail());
        response.setFotoPerfil(cliente.getFotoPerfil());

        return response;
    }
}
