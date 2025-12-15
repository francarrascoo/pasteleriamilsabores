package com.service.pasteleriamilsabores.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDto {
    private String pedidoId;
    private String status;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private BigDecimal shippingCost;
    private String deliveryAddress;
    private Boolean freeCakeApplied;
    private BigDecimal freeCakeAmount;
    private Integer discountAppliedPercent;
    private Integer lifetimeDiscountAppliedPercent;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal totalSinDescuento;
    private BigDecimal totalConDescuento;
    private String cardId;
    private String cardLastFour;
    private String purchaserRun;
    private String purchaserNombre;
    private String purchaserApellidos;
    private String purchaserCorreo;
    private String purchaserTelefono;
    private List<OrderItemSummaryDto> items;
}
