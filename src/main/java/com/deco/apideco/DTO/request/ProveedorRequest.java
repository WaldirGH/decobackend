package com.deco.apideco.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class ProveedorRequest {
    @NotBlank
    private String ruc;
    @NotBlank
    private String nombre;
}
