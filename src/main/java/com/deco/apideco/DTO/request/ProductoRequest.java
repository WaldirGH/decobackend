package com.deco.apideco.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
// ProductoRequest.java
public class ProductoRequest {

    @NotBlank
    private String nombre;

    @NotNull
    private Double precio;

    private String descripcion;

    @NotBlank
    private String codigoBarra;

    @NotNull
    private Long categoriaId;

    @NotNull
    private Long proveedorId;
    
}
