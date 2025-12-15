package com.service.pasteleriamilsabores.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDiariaDto {
    private Long id;
    private LocalDate fecha;
    private String productoCodiogo;
    private String productoNombre;
    private Integer cantidadVendida;
    private BigDecimal ingresosTotal;
}
