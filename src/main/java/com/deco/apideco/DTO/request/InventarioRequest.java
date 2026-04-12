package com.deco.apideco.DTO.request;

import com.deco.apideco.model.Enums.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventarioRequest {
    @NotNull
    private String codigoBarra;
    @NotNull
    private Integer cantidad;
    private String motivo;
    @NotNull
    private TipoMovimiento tipo;
}
