package com.deco.apideco.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deco.apideco.DTO.request.PedidoRequest;
import com.deco.apideco.DTO.response.PedidoResponse;
import com.deco.apideco.model.Enums.EstadoPedido;
import com.deco.apideco.services.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/public/pedidos")
    public ResponseEntity<PedidoResponse> crearSinLogin(
            @Valid @RequestBody PedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crear(request));
    }

    @PostMapping("/cliente/pedidos")
    public ResponseEntity<PedidoResponse> crearConLogin(
            @Valid @RequestBody PedidoRequest request,
            @AuthenticationPrincipal OAuth2User principal) {

        String googleId = principal.getAttribute("sub");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoService.crear(request, googleId));
    }

    @GetMapping("/cliente/pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(
            @AuthenticationPrincipal OAuth2User principal) {

        System.out.println("principal = " + principal);

        if (principal != null) {
            System.out.println("email = " + principal.getAttribute("email"));
        }

        String email = principal.getAttribute("email");
        return ResponseEntity.ok(pedidoService.listarPorEmailCliente(email));
    }

    @GetMapping("/cliente/pedidos/{id}")
    public ResponseEntity<PedidoResponse> detalleMiPedido(
            @PathVariable Long id,
            @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        return ResponseEntity.ok(pedidoService.obtenerPorIdYEmail(id, email));
    }

    @GetMapping("/test/pedidos")
    public ResponseEntity<List<PedidoResponse>> testPedidos(
            @RequestParam String email) {
        return ResponseEntity.ok(pedidoService.listarPorEmailCliente(email));
    }

    @GetMapping("/admin/pedidos")
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/admin/pedidos/estado/{estado}")
    public ResponseEntity<List<PedidoResponse>> listarPorEstado(
            @PathVariable EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.listarPorEstado(estado));
    }

    @PatchMapping("/admin/pedidos/{id}/estado")
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, estado));
    }

    @DeleteMapping("/admin/pedidos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}