package com.deco.apideco.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deco.apideco.DTO.request.InventarioRequest;
import com.deco.apideco.DTO.response.InventarioResponse;
import com.deco.apideco.DTO.response.MovimientoResponse;
import com.deco.apideco.model.Enums.TipoMovimiento;
import com.deco.apideco.services.InventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioResponse>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/producto/codigo/{codigoBarra}")
    public ResponseEntity<InventarioResponse> obtenerStockPorCodigo(@PathVariable String codigoBarra) {
        return ResponseEntity.ok(inventarioService.obtenerStockPorCodigo(codigoBarra));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioResponse> obtenerStock(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerStockPorProducto(productoId));
    }

    @GetMapping("/producto/{productoId}/historial")
    public ResponseEntity<List<MovimientoResponse>> historialPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.historialPorProducto(productoId));
    }

    @GetMapping("/historial/{tipo}")
    public ResponseEntity<List<MovimientoResponse>> historialPorTipo(@PathVariable TipoMovimiento tipo) {
        return ResponseEntity.ok(inventarioService.historialPorTipo(tipo));
    }

    @PostMapping("/movimiento")
    public ResponseEntity<InventarioResponse> registrarMovimiento(
            @Valid @RequestBody InventarioRequest request) {
        return ResponseEntity.ok(inventarioService.registrarMovimiento(request));
    }
}
