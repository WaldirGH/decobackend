package com.deco.apideco.services;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.deco.apideco.DTO.response.ImagenResponse;
import com.deco.apideco.model.Imagen;

public interface ImagenService {
    Imagen uploadImage(MultipartFile file) throws Exception;
    void deleteImage(String publicId) throws Exception;
    // ← agregar estos
    List<ImagenResponse> subirImagenes(Long productoId, List<MultipartFile> archivos) throws Exception;
    List<ImagenResponse> listarPorProducto(Long productoId);
}