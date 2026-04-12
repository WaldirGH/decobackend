package com.deco.apideco.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.deco.apideco.DTO.response.ImagenResponse;
import com.deco.apideco.model.Imagen;
import com.deco.apideco.model.Producto;
import com.deco.apideco.repo.ImagenRepo;
import com.deco.apideco.repo.ProductoRepo;
import com.deco.apideco.services.CloudinaryService;
import com.deco.apideco.services.ImagenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImagenServiceImpl implements ImagenService {

    private final CloudinaryService cloudinaryService;
    private final ImagenRepo imagenRepo;
    private final ProductoRepo productoRepo;

    @Override
    public Imagen uploadImage(MultipartFile file) throws Exception {
        Map<?, ?> result = cloudinaryService.upload(file);

        Imagen imagen = new Imagen();
        imagen.setNombre(file.getOriginalFilename());
        imagen.setImgUrl((String) result.get("url"));
        imagen.setImgId((String) result.get("public_id")); // ← cambia "publicId" por "public_id"

        return imagenRepo.save(imagen);
    }

    @Override
    public void deleteImage(String publicId) throws Exception {
        cloudinaryService.delete(publicId);
        imagenRepo.deleteByImgId(publicId);
    }

    @Override
    public List<ImagenResponse> subirImagenes(Long productoId, List<MultipartFile> archivos) throws Exception {
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + productoId));

        List<ImagenResponse> responses = new ArrayList<>();
        int index = imagenRepo.findByProductoId(productoId).size() + 1; // ← continúa desde la última imagen

        for (MultipartFile archivo : archivos) {
            String publicId = producto.getCodigoBarra() + "_" + index;
            Map<?, ?> result = cloudinaryService.upload(archivo, publicId); // ← nuevo método
            index++;

            Imagen imagen = new Imagen();
            imagen.setNombre(archivo.getOriginalFilename());
            imagen.setImgUrl((String) result.get("url"));
            imagen.setImgId((String) result.get("public_id"));
            imagen.setProducto(producto);

            Imagen guardada = imagenRepo.save(imagen);
            responses.add(toResponse(guardada));
        }
        return responses;
    }

    @Override
    public List<ImagenResponse> listarPorProducto(Long productoId) {
        return imagenRepo.findByProductoId(productoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ImagenResponse toResponse(Imagen imagen) {
        ImagenResponse response = new ImagenResponse();
        response.setId(imagen.getId());
        response.setNombre(imagen.getNombre());
        response.setImgUrl(imagen.getImgUrl());
        response.setImgId(imagen.getImgId());
        return response;
    }
}
