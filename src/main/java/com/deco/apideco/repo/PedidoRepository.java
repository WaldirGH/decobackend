package com.deco.apideco.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deco.apideco.model.Enums.EstadoPedido;
import com.deco.apideco.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstadoOrderByIdDesc(EstadoPedido estado);

    List<Pedido> findByClienteGoogleIdOrderByIdDesc(String googleId);

    List<Pedido> findByEmailOrderByIdDesc(String email);

    Optional<Pedido> findByIdAndEmail(Long id, String email);
}