package com.deco.apideco.services;

import com.deco.apideco.DTO.response.ClienteResponse;

public interface ClienteService {
    ClienteResponse obtenerPerfilPorGoogleId(String googleId);
}
