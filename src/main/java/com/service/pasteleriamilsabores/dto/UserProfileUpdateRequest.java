package com.service.pasteleriamilsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String nombre;
    private String apellidos;
    private String correo;
    private String fechaNacimiento;
    private String telefono;
    private String tipoUsuario;
}