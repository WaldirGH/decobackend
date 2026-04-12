package com.deco.apideco.services;

import java.util.List;

import com.deco.apideco.DTO.request.ProveedorRequest;
import com.deco.apideco.DTO.response.ProveedorResponse;

public interface ProveedorService {
    ProveedorResponse crear(ProveedorRequest request);
    ProveedorResponse obtenerPorId(Long id);
    List<ProveedorResponse> listarTodos();
    ProveedorResponse actualizar(Long id, ProveedorRequest request);
    void eliminar(Long id);
}
