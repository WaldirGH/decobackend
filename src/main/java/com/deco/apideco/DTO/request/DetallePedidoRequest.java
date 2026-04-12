package com.deco.apideco.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class DetallePedidoRequest {
    @NotNull
    private Long productoId;
    @NotNull
    private Long imagenElegidaId;   // imagen que eligió el cliente
    @NotNull
    private Integer cantidad;
}