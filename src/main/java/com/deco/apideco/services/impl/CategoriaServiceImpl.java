package com.deco.apideco.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deco.apideco.DTO.request.CategoriaRequest;
import com.deco.apideco.DTO.response.CategoriaResponse;
import com.deco.apideco.model.Categoria;
import com.deco.apideco.repo.CategoriaRepo;
import com.deco.apideco.services.CategoriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepo categoriaRepo;

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        return toResponse(categoriaRepo.save(categoria));
    }

    @Override
    public CategoriaResponse obtenerPorId(Long id) {
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
        return toResponse(categoria);
    }

    @Override
    public List<CategoriaResponse> listarTodos() {
        return categoriaRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
        categoria.setNombre(request.getNombre());
        return toResponse(categoriaRepo.save(categoria));
    }

    @Override
    public void eliminar(Long id) {
        if (!categoriaRepo.existsById(id))
            throw new RuntimeException("Categoria no encontrada con id: " + id);
        categoriaRepo.deleteById(id);
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        CategoriaResponse response = new CategoriaResponse();
        response.setId(categoria.getId());
        response.setNombre(categoria.getNombre());
        return response;
    }
}