package com.service.pasteleriamilsabores.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String pedidoId;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal freeCakeAmount;
    private Integer discountPercentApplied;
    private Integer lifetimeDiscountPercentApplied;
    private BigDecimal discountAmount;
    private BigDecimal total;
    private String cardId;
    private String cardLastFour;
    private List<Object> items;
    private Boolean freeCakeAvailable;
    private java.util.List<String> freeCakeSuggestedProducts;
}
