package com.deco.apideco.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.deco.apideco.model.Enums.EstadoPedido;
import com.deco.apideco.model.Enums.TipoComprobante;
import com.deco.apideco.model.Enums.TipoEntrega;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del cliente (logueado o no)
    private String nombreCliente;
    private String email;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private TipoEntrega tipoEntrega; // DELIVERY / RECOJO_TIENDA

    private String direccion; // solo si es DELIVERY
    private String quienRecoge; // solo si es RECOJO_TIENDA
    private String observacion;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado; // PENDIENTE → EN_PROCESO → ENVIADO → ENTREGADO

    @Enumerated(EnumType.STRING)
    private TipoComprobante tipoComprobante; // BOLETA / FACTURA

    // Solo si es FACTURA
    private String ruc;
    private String razonSocial;

    private Double total;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    // null si no está logueado
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetallePedido> detalles = new ArrayList<>();
}