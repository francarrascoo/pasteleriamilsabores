package com.service.pasteleriamilsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDto {
    private String id;
    private String titulo;
    private String resumen;
    private String imagen;
    private String fecha;
    private String autor;
    private String link;
}
