package com.deco.apideco.services.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.deco.apideco.DTO.request.ProductoRequest;
import com.deco.apideco.DTO.response.CategoriaResponse;
import com.deco.apideco.DTO.response.ImagenResponse;
import com.deco.apideco.DTO.response.ProductoResponse;
import com.deco.apideco.DTO.response.ProveedorResponse;
import com.deco.apideco.model.Categoria;
import com.deco.apideco.model.Imagen;
import com.deco.apideco.model.Producto;
import com.deco.apideco.model.Proveedor;
import com.deco.apideco.repo.CategoriaRepo;
import com.deco.apideco.repo.ImagenRepo;
import com.deco.apideco.repo.ProductoRepo;
import com.deco.apideco.repo.ProveedorRepo;
import com.deco.apideco.services.CloudinaryService;
import com.deco.apideco.services.ProductoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepo productoRepo;
    private final CategoriaRepo categoriaRepo;
    private final ProveedorRepo proveedorRepo;
    private final CloudinaryService cloudinaryService;
    private final ImagenRepo imagenRepo;

    @Override
    public List<ProductoResponse> listarPorCategoria(Long categoriaId) {
        return productoRepo.findByCategoriaId(categoriaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductoResponse crear(ProductoRequest request, List<MultipartFile> archivos) throws Exception {
        Producto producto = buildProducto(new Producto(), request);
        producto = productoRepo.save(producto);

        // Subir imágenes si vienen en el request
        if (archivos != null && !archivos.isEmpty()) {
            subirYGuardarImagenes(producto, archivos);
        }
        productoRepo.flush();
        return toResponse(productoRepo.findById(producto.getId()).orElseThrow());
    }

    @Override
    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request, List<MultipartFile> archivos)
            throws Exception {
        Producto producto = findById(id);
        buildProducto(producto, request);
        productoRepo.save(producto);

        if (archivos != null && !archivos.isEmpty()) {
            subirYGuardarImagenes(producto, archivos);
        }

        return toResponse(productoRepo.findById(producto.getId()).orElseThrow());
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public List<ProductoResponse> listarTodos() {
        return productoRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long id) throws Exception {
        Producto producto = findById(id);

        // Eliminar imágenes de Cloudinary antes de borrar el producto
        for (Imagen imagen : producto.getImagenes()) {
            cloudinaryService.delete(imagen.getImgId());
        }

        productoRepo.delete(producto);
    }

    // ── helpers ──────────────────────────────────────────────

    private void subirYGuardarImagenes(Producto producto, List<MultipartFile> archivos) throws Exception {
        int index = imagenRepo.findByProductoId(producto.getId()).size() + 1;
        for (MultipartFile archivo : archivos) {
            String publicId = producto.getCodigoBarra() + "_" + index;
            Map<?, ?> resultado = cloudinaryService.upload(archivo, publicId);
            index++;

            Imagen imagen = new Imagen();
            imagen.setNombre(archivo.getOriginalFilename());
            imagen.setImgUrl((String) resultado.get("url"));
            imagen.setImgId((String) resultado.get("public_id"));
            imagen.setProducto(producto);

            Imagen guardada = imagenRepo.save(imagen);
            producto.getImagenes().add(guardada); // ← agrega a la lista en memoria
        }
    }

    private Producto findById(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    private Producto buildProducto(Producto producto, ProductoRequest request) {
        Categoria categoria = categoriaRepo.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + request.getCategoriaId()));
        Proveedor proveedor = proveedorRepo.findById(request.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + request.getProveedorId()));

        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setDescripcion(request.getDescripcion());
        producto.setCodigoBarra(request.getCodigoBarra());
        producto.setCategoria(categoria);
        producto.setProveedor(proveedor);
        return producto;
    }

    private ProductoResponse toResponse(Producto producto) {
        CategoriaResponse catResp = new CategoriaResponse();
        catResp.setId(producto.getCategoria().getId());
        catResp.setNombre(producto.getCategoria().getNombre());

        ProveedorResponse provResp = new ProveedorResponse();
        provResp.setId(producto.getProveedor().getId());
        provResp.setRuc(producto.getProveedor().getRuc());
        provResp.setNombre(producto.getProveedor().getNombre());

        List<ImagenResponse> imagenesResp = producto.getImagenes() == null
                ? List.of()
                : producto.getImagenes().stream().map(img -> {
                    ImagenResponse ir = new ImagenResponse();
                    ir.setId(img.getId());
                    ir.setNombre(img.getNombre());
                    ir.setImgUrl(img.getImgUrl());
                    ir.setImgId(img.getImgId());
                    return ir;
                }).toList();

        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setPrecio(producto.getPrecio());
        response.setDescripcion(producto.getDescripcion());
        response.setCodigoBarra(producto.getCodigoBarra());
        response.setCategoria(catResp);
        response.setProveedor(provResp);
        response.setImagenes(imagenesResp);
        return response;
    }
}