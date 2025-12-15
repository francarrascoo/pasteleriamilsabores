package com.service.pasteleriamilsabores.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String run;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String fechaNacimiento;
    private String tipoUsuario;
    private List<AddressDto> addresses;
    private List<CardDto> cards;
    private Boolean activo;
}
