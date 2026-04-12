package com.deco.apideco.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ProveedorResponse {
    private Long id;
    private String ruc;
    private String nombre;
}
