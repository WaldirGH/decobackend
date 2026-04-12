package com.deco.apideco.services;

import java.util.List;

import com.deco.apideco.DTO.request.InventarioRequest;
import com.deco.apideco.DTO.response.InventarioResponse;
import com.deco.apideco.DTO.response.MovimientoResponse;
import com.deco.apideco.model.Pedido;
import com.deco.apideco.model.Producto;
import com.deco.apideco.model.Enums.TipoMovimiento;

public interface InventarioService {

    InventarioResponse registrarMovimiento(InventarioRequest request);
    InventarioResponse obtenerStockPorProducto(Long productoId);
    InventarioResponse obtenerStockPorCodigo(String codigoBarra);
    List<InventarioResponse> listarTodos();
    List<MovimientoResponse> historialPorProducto(Long productoId);
    List<MovimientoResponse> historialPorTipo(TipoMovimiento tipo);
    // usado internamente al crear pedido
    void descontarStockPorPedido(Producto producto, Integer cantidad, Pedido pedido);
}
