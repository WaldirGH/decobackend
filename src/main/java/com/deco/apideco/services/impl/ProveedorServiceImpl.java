package com.deco.apideco.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deco.apideco.DTO.request.ProveedorRequest;
import com.deco.apideco.DTO.response.ProveedorResponse;
import com.deco.apideco.model.Proveedor;
import com.deco.apideco.repo.ProveedorRepo;
import com.deco.apideco.services.ProveedorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepo proveedorRepo;

    @Override
    public ProveedorResponse crear(ProveedorRequest request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setRuc(request.getRuc());
        proveedor.setNombre(request.getNombre());
        return toResponse(proveedorRepo.save(proveedor));
    }

    @Override
    public ProveedorResponse obtenerPorId(Long id) {
        Proveedor proveedor = proveedorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
        return toResponse(proveedor);
    }

    @Override
    public List<ProveedorResponse> listarTodos() {
        return proveedorRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProveedorResponse actualizar(Long id, ProveedorRequest request) {
        Proveedor proveedor = proveedorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
        proveedor.setRuc(request.getRuc());
        proveedor.setNombre(request.getNombre());
        return toResponse(proveedorRepo.save(proveedor));
    }

    @Override
    public void eliminar(Long id) {
        if (!proveedorRepo.existsById(id))
            throw new RuntimeException("Proveedor no encontrado con id: " + id);
        proveedorRepo.deleteById(id);
    }

    private ProveedorResponse toResponse(Proveedor proveedor) {
        ProveedorResponse response = new ProveedorResponse();
        response.setId(proveedor.getId());
        response.setRuc(proveedor.getRuc());
        response.setNombre(proveedor.getNombre());
        return response;
    }
}
