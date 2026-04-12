package com.deco.apideco.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deco.apideco.DTO.request.DetallePedidoRequest;
import com.deco.apideco.DTO.request.PedidoRequest;
import com.deco.apideco.DTO.response.ClienteResponse;
import com.deco.apideco.DTO.response.DetallePedidoResponse;
import com.deco.apideco.DTO.response.PedidoResponse;
import com.deco.apideco.model.Cliente;
import com.deco.apideco.model.DetallePedido;
import com.deco.apideco.model.Enums.EstadoPedido;
import com.deco.apideco.model.Pedido;
import com.deco.apideco.model.Producto;
import com.deco.apideco.repo.ClienteRepository;
import com.deco.apideco.repo.PedidoRepository;
import com.deco.apideco.repo.ProductoRepo;
import com.deco.apideco.services.PedidoService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepo productoRepository;
    private final ClienteRepository clienteRepository;

    @Override
    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        Pedido pedido = new Pedido();

        pedido.setNombreCliente(request.getNombreCliente());
        pedido.setEmail(request.getEmail());
        pedido.setTelefono(request.getTelefono());
        pedido.setTipoEntrega(request.getTipoEntrega());
        pedido.setDireccion(request.getDireccion());
        pedido.setQuienRecoge(request.getQuienRecoge());
        pedido.setObservacion(request.getObservacion());
        pedido.setTipoComprobante(request.getTipoComprobante());
        pedido.setRuc(request.getRuc());
        pedido.setRazonSocial(request.getRazonSocial());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        Double total = 0.0;

        if (request.getDetalles() != null) {
            for (DetallePedidoRequest detReq : request.getDetalles()) {
                Producto producto = productoRepository.findById(detReq.getProductoId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Producto no encontrado con id: " + detReq.getProductoId()));

                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);
                detalle.setProducto(producto);
                detalle.setCantidad(detReq.getCantidad());

                Double subtotal = producto.getPrecio() * detReq.getCantidad();
                detalle.setSubtotal(subtotal);

                if (pedido.getDetalles() != null) {
                    pedido.getDetalles().add(detalle);
                }

                total += subtotal;
            }
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        return toResponse(guardado);
    }

    @Override
    @Transactional
    public PedidoResponse crear(PedidoRequest request, String googleId) {
        Cliente cliente = clienteRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

        Pedido pedido = new Pedido();

        pedido.setCliente(cliente);
        pedido.setNombreCliente(request.getNombreCliente());
        pedido.setEmail(request.getEmail());
        pedido.setTelefono(request.getTelefono());
        pedido.setTipoEntrega(request.getTipoEntrega());
        pedido.setDireccion(request.getDireccion());
        pedido.setQuienRecoge(request.getQuienRecoge());
        pedido.setObservacion(request.getObservacion());
        pedido.setTipoComprobante(request.getTipoComprobante());
        pedido.setRuc(request.getRuc());
        pedido.setRazonSocial(request.getRazonSocial());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        Double total = 0.0;

        if (request.getDetalles() != null) {
            for (DetallePedidoRequest detReq : request.getDetalles()) {
                Producto producto = productoRepository.findById(detReq.getProductoId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Producto no encontrado con id: " + detReq.getProductoId()));

                DetallePedido detalle = new DetallePedido();
                detalle.setPedido(pedido);
                detalle.setProducto(producto);
                detalle.setCantidad(detReq.getCantidad());

                Double subtotal = producto.getPrecio() * detReq.getCantidad();
                detalle.setSubtotal(subtotal);

                if (pedido.getDetalles() != null) {
                    pedido.getDetalles().add(detalle);
                }

                total += subtotal;
            }
        }

        pedido.setTotal(total);

        Pedido guardado = pedidoRepository.save(pedido);
        return toResponse(guardado);
    }

    @Override
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PedidoResponse> listarPorEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstadoOrderByIdDesc(estado)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PedidoResponse cambiarEstado(Long id, EstadoPedido estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        pedido.setEstado(estado);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Override
    public void eliminar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedido no encontrado");
        }
        pedidoRepository.deleteById(id);
    }

    @Override
    public PedidoResponse obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
        return toResponse(pedido);
    }

    @Override
    public List<PedidoResponse> listarPorClienteGoogleId(String googleId) {
        return pedidoRepository.findByClienteGoogleIdOrderByIdDesc(googleId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<PedidoResponse> listarPorEmailCliente(String email) {
        return pedidoRepository.findByEmailOrderByIdDesc(email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public PedidoResponse obtenerPorIdYEmail(Long id, String email) {
        Pedido pedido = pedidoRepository.findByIdAndEmail(id, email)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado para este cliente"));
        return toResponse(pedido);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();

        response.setId(pedido.getId());
        response.setNombreCliente(pedido.getNombreCliente());
        response.setEmail(pedido.getEmail());
        response.setTelefono(pedido.getTelefono());
        response.setTipoEntrega(pedido.getTipoEntrega());
        response.setDireccion(pedido.getDireccion());
        response.setQuienRecoge(pedido.getQuienRecoge());
        response.setObservacion(pedido.getObservacion());
        response.setTipoComprobante(pedido.getTipoComprobante());
        response.setRuc(pedido.getRuc());
        response.setRazonSocial(pedido.getRazonSocial());
        response.setEstado(pedido.getEstado());
        response.setTotal(pedido.getTotal());
        response.setFechaCreacion(pedido.getFechaCreacion());

        if (pedido.getCliente() != null) {
            ClienteResponse clienteResponse = new ClienteResponse();
            clienteResponse.setId(pedido.getCliente().getId());
            clienteResponse.setNombre(pedido.getCliente().getNombre());
            clienteResponse.setEmail(pedido.getCliente().getEmail());
            clienteResponse.setFotoPerfil(pedido.getCliente().getFotoPerfil());
            response.setCliente(clienteResponse);
        }

        if (pedido.getDetalles() != null) {
            response.setDetalles(
                    pedido.getDetalles().stream().map(det -> {
                        DetallePedidoResponse d = new DetallePedidoResponse();

                        d.setNombreProducto(det.getProducto().getNombre());
                        d.setCantidad(det.getCantidad());
                        d.setSubtotal(det.getSubtotal());

                        return d;
                    }).toList()
            );
        }

        return response;
    }
}