package com.service.pasteleriamilsabores.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private String id;
    private String userRun;
    private List<CartItemDto> items = new ArrayList<>();
    private Integer total;
}
