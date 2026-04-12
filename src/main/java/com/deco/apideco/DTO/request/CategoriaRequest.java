package com.deco.apideco.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CategoriaRequest {
    @NotBlank
    private String nombre;
}
