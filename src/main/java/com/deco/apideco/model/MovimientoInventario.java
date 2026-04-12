package com.deco.apideco.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.deco.apideco.model.Enums.TipoMovimiento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movimientos_inventario")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;    // ENTRADA / SALIDA / AJUSTE

    private Integer cantidad;
    private String motivo;          // descripción del movimiento
    private Integer stockAnterior;
    private Integer stockNuevo;

    @CreationTimestamp
    private LocalDateTime fecha;

    // null si es ENTRADA o AJUSTE, se llena si es SALIDA por pedido
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
}
