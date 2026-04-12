package com.deco.apideco.services;

import java.util.List;

import com.deco.apideco.DTO.request.PedidoRequest;
import com.deco.apideco.DTO.response.PedidoResponse;
import com.deco.apideco.model.Enums.EstadoPedido;

public interface PedidoService {

    PedidoResponse crear(PedidoRequest request);

    PedidoResponse crear(PedidoRequest request, String googleId);

    List<PedidoResponse> listarTodos();

    List<PedidoResponse> listarPorEstado(EstadoPedido estado);

    PedidoResponse cambiarEstado(Long id, EstadoPedido estado);

    void eliminar(Long id);

    PedidoResponse obtenerPorId(Long id);

    List<PedidoResponse> listarPorClienteGoogleId(String googleId);

    List<PedidoResponse> listarPorEmailCliente(String email);

    PedidoResponse obtenerPorIdYEmail(Long id, String email);
}