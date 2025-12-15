package com.service.pasteleriamilsabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String run;
    private String nombre;
    private String apellidos;
    private String correo;
    private String fechaNacimiento; // yyyy-MM-dd
    private String telefono;
    private String tipoUsuario;
    private String password;
    private String registrationCode; // optional, e.g., FELICES50
}
