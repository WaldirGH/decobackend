package com.deco.apideco.services;

import java.util.List;

import com.deco.apideco.DTO.request.UsuarioRequest;
import com.deco.apideco.DTO.response.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse crear(UsuarioRequest request);
    UsuarioResponse obtenerPorId(Long id);
    List<UsuarioResponse> listarTodos();
    UsuarioResponse actualizar(Long id, UsuarioRequest request);
    void eliminar(Long id);
}
