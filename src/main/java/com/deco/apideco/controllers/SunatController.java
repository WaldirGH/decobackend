package com.deco.apideco.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deco.apideco.DTO.response.SunatRucResponse;
import com.deco.apideco.services.SunatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/sunat")
@RequiredArgsConstructor
public class SunatController {

    private final SunatService sunatService;

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<SunatRucResponse> consultarRuc(@PathVariable String ruc) {
        return ResponseEntity.ok(sunatService.consultarRuc(ruc));
    }
}