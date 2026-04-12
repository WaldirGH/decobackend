package com.deco.apideco.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ImagenResponse {
    private Long id;
    private String nombre;
    private String imgUrl;
    private String imgId;
}
