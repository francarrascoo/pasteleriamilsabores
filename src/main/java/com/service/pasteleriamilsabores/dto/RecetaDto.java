package com.service.pasteleriamilsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDto {
    private String id;
    private String titulo;
    private String resumen;
    private String ingredientes;
    private String preparacion;
    private String imagen;
    private String badge;
    private String badgeClass;
}
