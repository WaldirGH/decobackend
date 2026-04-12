package com.deco.apideco.DTO.request;

import java.util.List;

import com.deco.apideco.model.Enums.TipoComprobante;
import com.deco.apideco.model.Enums.TipoEntrega;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PedidoRequest {

    @NotBlank
    private String nombreCliente;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String telefono;

    @NotNull
    private TipoEntrega tipoEntrega;

    private String direccion;       // requerido si tipoEntrega = DELIVERY
    private String quienRecoge;     // requerido si tipoEntrega = RECOJO_TIENDA
    private String observacion;

    @NotNull
    private TipoComprobante tipoComprobante;

    private String ruc;             // requerido si tipoComprobante = FACTURA
    private String razonSocial;     // requerido si tipoComprobante = FACTURA

    @NotEmpty
    private List<DetallePedidoRequest> detalles;
}
