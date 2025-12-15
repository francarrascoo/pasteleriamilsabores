package com.service.pasteleriamilsabores.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private Long id;
    private String userRun;
    private String userName;
    private String productoCodigoProducto;
    private Integer stars;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
