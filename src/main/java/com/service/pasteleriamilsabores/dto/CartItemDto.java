package com.service.pasteleriamilsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String id;
    private String productoCodigo;
    private Integer cantidad;
    private Integer precioUnitario;
    private Integer precioTotal;
}
