package com.deco.apideco.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deco.apideco.services.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final MercadoPagoService mercadoPagoService;

    @PostMapping("/crear-preferencia")
    public ResponseEntity<?> crearPreferencia(@RequestBody Map<String, Object> data) {
        try {
            Double monto = Double.valueOf(data.get("total").toString());
            String descripcion = data.get("titulo").toString();
            String initPoint = mercadoPagoService.crearPreferencia(monto, descripcion);
            return ResponseEntity.ok(Map.of("initPoint", initPoint));

        } catch (MPApiException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Error API Mercado Pago",
                "detalle", e.getApiResponse() != null ? e.getApiResponse().getContent() : e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "message", "Error al crear pago",
                "error", e.getMessage()
            ));
        }
    }
}