package com.deco.apideco.DTO.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class InventarioResponse {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer stock;
    private LocalDateTime fechaActualizacion;
}
