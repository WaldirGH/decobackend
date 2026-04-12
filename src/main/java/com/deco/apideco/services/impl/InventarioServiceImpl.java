package com.deco.apideco.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deco.apideco.DTO.request.InventarioRequest;
import com.deco.apideco.DTO.response.InventarioResponse;
import com.deco.apideco.DTO.response.MovimientoResponse;
import com.deco.apideco.model.Inventario;
import com.deco.apideco.model.MovimientoInventario;
import com.deco.apideco.model.Pedido;
import com.deco.apideco.model.Producto;
import com.deco.apideco.model.Enums.TipoMovimiento;
import com.deco.apideco.repo.InventarioRepository;
import com.deco.apideco.repo.MovimientoInventarioRepository;
import com.deco.apideco.repo.ProductoRepo;
import com.deco.apideco.services.InventarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final ProductoRepo productoRepository;

    @Override
    @Transactional
    public InventarioResponse registrarMovimiento(InventarioRequest request) {
        Producto producto = productoRepository.findByCodigoBarra(request.getCodigoBarra())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con código: " + request.getCodigoBarra()));

        Inventario inventario = inventarioRepository.findByProductoId(producto.getId())
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setProducto(producto);
                    nuevo.setStock(0);
                    return nuevo;
                });

        int stockAnterior = inventario.getStock();
        int stockNuevo;

        switch (request.getTipo()) {
            case ENTRADA -> stockNuevo = stockAnterior + request.getCantidad();
            case SALIDA -> {
                if (stockAnterior < request.getCantidad())
                    throw new RuntimeException("Stock insuficiente. Stock actual: " + stockAnterior);
                stockNuevo = stockAnterior - request.getCantidad();
            }
            case AJUSTE -> stockNuevo = request.getCantidad(); // ajuste directo al valor
            default -> throw new RuntimeException("Tipo de movimiento no válido");
        }

        inventario.setStock(stockNuevo);
        inventario.setFechaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);

        // registrar movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipo(request.getTipo());
        movimiento.setCantidad(request.getCantidad());
        movimiento.setMotivo(request.getMotivo());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimientoRepository.save(movimiento);

        return toInventarioResponse(inventario);
    }

    @Override
    public InventarioResponse obtenerStockPorProducto(Long productoId) {
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No hay inventario para este producto"));
        return toInventarioResponse(inventario);
    }

    @Override
    public List<InventarioResponse> listarTodos() {
        return inventarioRepository.findAll()
                .stream().map(this::toInventarioResponse).toList();
    }

    @Override
    public List<MovimientoResponse> historialPorProducto(Long productoId) {
        return movimientoRepository.findByProductoIdOrderByFechaDesc(productoId)
                .stream().map(this::toMovimientoResponse).toList();
    }

    @Override
    public List<MovimientoResponse> historialPorTipo(TipoMovimiento tipo) {
        return movimientoRepository.findByTipoOrderByFechaDesc(tipo)
                .stream().map(this::toMovimientoResponse).toList();
    }

    @Override
    public InventarioResponse obtenerStockPorCodigo(String codigoBarra) {
        Producto producto = productoRepository.findByCodigoBarra(codigoBarra)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con código: " + codigoBarra));
        Inventario inventario = inventarioRepository.findByProductoId(producto.getId())
                .orElseThrow(() -> new RuntimeException("No hay inventario para este producto"));
        return toInventarioResponse(inventario);
    }

    @Override
    @Transactional
    public void descontarStockPorPedido(Producto producto, Integer cantidad, Pedido pedido) {
        Inventario inventario = inventarioRepository.findByProductoId(producto.getId())
                .orElseThrow(() -> new RuntimeException(
                        "No hay inventario registrado para: " + producto.getNombre()));

        if (inventario.getStock() < cantidad)
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre()
                    + ". Stock actual: " + inventario.getStock());

        int stockAnterior = inventario.getStock();
        int stockNuevo = stockAnterior - cantidad;

        inventario.setStock(stockNuevo);
        inventario.setFechaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setTipo(TipoMovimiento.SALIDA);
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo("Pedido #" + pedido.getId());
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setPedido(pedido);
        movimientoRepository.save(movimiento);
    }

    // ── helpers ──────────────────────────────────────────────

    private InventarioResponse toInventarioResponse(Inventario inventario) {
        return new InventarioResponse(
                inventario.getId(),
                inventario.getProducto().getId(),
                inventario.getProducto().getNombre(),
                inventario.getStock(),
                inventario.getFechaActualizacion());
    }

    private MovimientoResponse toMovimientoResponse(MovimientoInventario m) {
        return new MovimientoResponse(
                m.getId(),
                m.getProducto().getNombre(),
                m.getTipo(),
                m.getCantidad(),
                m.getMotivo(),
                m.getStockAnterior(),
                m.getStockNuevo(),
                m.getFecha(),
                m.getPedido() != null ? m.getPedido().getId() : null);
    }
}
