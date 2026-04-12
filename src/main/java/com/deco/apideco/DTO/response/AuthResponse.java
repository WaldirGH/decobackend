package com.deco.apideco.DTO.response;

import com.deco.apideco.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String nombre;
    private String email;
    private Rol rol;
}
