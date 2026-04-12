package com.deco.apideco.DTO.request;

import com.deco.apideco.model.Rol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UsuarioRequest {
    @NotBlank
    private String nombre;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Rol rol;
}

