package com.deco.apideco.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deco.apideco.DTO.response.SunatRucResponse;

@Service
public class SunatService {

    @Value("${decolecta.api.url}")
    private String apiUrl;

    @Value("${decolecta.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate;

    public SunatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SunatRucResponse consultarRuc(String ruc) {
        if (ruc == null || !Pattern.matches("\\d{11}", ruc)) {
            throw new RuntimeException("El RUC debe tener 11 dígitos");
        }

        String url = apiUrl + "/sunat/ruc?numero=" + ruc;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<SunatRucResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                SunatRucResponse.class
        );

        return response.getBody();
    }
}
