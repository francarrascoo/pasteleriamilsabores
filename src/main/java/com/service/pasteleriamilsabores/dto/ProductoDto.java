package com.service.pasteleriamilsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDto {
    private String codigoProducto;
    private String nombreProducto;
    private Integer precioProducto;
    private String descripcionProducto;
    private String imagenProducto;
    private Integer stock;
    private Integer stockCritico;
    private Integer categoriaId;
    private String categoriaNombre;
}
