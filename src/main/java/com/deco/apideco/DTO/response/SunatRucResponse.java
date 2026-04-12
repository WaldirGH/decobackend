package com.deco.apideco.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class SunatRucResponse {
    
    private String razon_social;
    private String numero_documento;
    private String estado;
    private String condicion;
    private String direccion;
}
