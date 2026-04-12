package com.deco.apideco.DTO.response;

import java.time.LocalDateTime;
import com.deco.apideco.model.Enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MovimientoResponse {
    private Long id;
    private String nombreProducto;
    private TipoMovimiento tipo;
    private Integer cantidad;
    private String motivo;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private LocalDateTime fecha;
    private Long pedidoId;  // null si no es SALIDA por pedido
}
