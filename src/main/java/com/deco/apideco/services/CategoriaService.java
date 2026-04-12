package com.deco.apideco.services;

import java.util.List;

import com.deco.apideco.DTO.request.CategoriaRequest;
import com.deco.apideco.DTO.response.CategoriaResponse;

public interface CategoriaService {
    CategoriaResponse crear(CategoriaRequest request);
    CategoriaResponse obtenerPorId(Long id);
    List<CategoriaResponse> listarTodos();
    CategoriaResponse actualizar(Long id, CategoriaRequest request);
    void eliminar(Long id);
}