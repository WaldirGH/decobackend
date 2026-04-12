package com.deco.apideco.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DetallePedidoResponse {
    private Long id;
    private String nombreProducto;
    private String imgUrl;          // imagen elegida
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
