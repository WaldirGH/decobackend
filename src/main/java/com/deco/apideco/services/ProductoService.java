package com.deco.apideco.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.deco.apideco.DTO.request.ProductoRequest;
import com.deco.apideco.DTO.response.ProductoResponse;

public interface ProductoService {

    List<ProductoResponse> listarPorCategoria(Long categoriaId);
    ProductoResponse crear(ProductoRequest request, List<MultipartFile> archivos) throws Exception;
    ProductoResponse obtenerPorId(Long id);
    List<ProductoResponse> listarTodos();
    ProductoResponse actualizar(Long id, ProductoRequest request, List<MultipartFile> archivos) throws Exception;
    void eliminar(Long id) throws Exception;
}
