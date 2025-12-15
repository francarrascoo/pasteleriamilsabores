package com.service.pasteleriamilsabores.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderRequest {
    private String userRun;
    private List<OrderItemRequest> items;
    private String deliveryAddress;
    private Boolean applyDiscounts;
    private Boolean applyFreeCakeCoupon;
    private String cardId;

    @JsonProperty("run")
    public void setRun(String run) {
        this.userRun = run;
    }

    @JsonProperty("direccionEntrega")
    public void setDireccionEntrega(String direccionEntrega) { this.deliveryAddress = direccionEntrega; }

    @JsonProperty("usarCuponTorta")
    public void setUsarCuponTorta(Boolean usarCuponTorta) { this.applyFreeCakeCoupon = usarCuponTorta; }

    @JsonProperty("cardId")
    public void setCardIdAlias(String cardId) { this.cardId = cardId; }
}
