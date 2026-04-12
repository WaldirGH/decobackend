package com.deco.apideco.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ClienteResponse {
    private Long id;
    private String nombre;
    private String email;
    private String fotoPerfil;
}
