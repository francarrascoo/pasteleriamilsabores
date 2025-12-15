package com.service.pasteleriamilsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String id;
    private String cardNumber;
    private Integer month;
    private Integer year;
    private String cardholderName;
    private String lastFourDigits;
    private Boolean isDefault;
}
