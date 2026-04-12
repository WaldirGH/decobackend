package com.deco.apideco.DTO.response;

import java.time.LocalDateTime;
import java.util.List;

import com.deco.apideco.model.Enums.EstadoPedido;
import com.deco.apideco.model.Enums.TipoComprobante;
import com.deco.apideco.model.Enums.TipoEntrega;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private String nombreCliente;
    private String email;
    private String telefono;
    private TipoEntrega tipoEntrega;
    private String direccion;
    private String quienRecoge;
    private String observacion;
    private EstadoPedido estado;
    private TipoComprobante tipoComprobante;
    private String ruc;
    private String razonSocial;
    private Double total;
    private LocalDateTime fechaCreacion;
    private ClienteResponse cliente;
    private List<DetallePedidoResponse> detalles;
}
