package com.deco.apideco.DTO.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter

public class ProductoResponse {
    private Long id;
    private String nombre;
    private Double precio;
    private String descripcion;
    private String codigoBarra;
    private CategoriaResponse categoria;
    private ProveedorResponse proveedor;
    private List<ImagenResponse> imagenes; 
}