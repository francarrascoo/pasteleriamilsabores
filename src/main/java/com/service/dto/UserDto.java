package com.service.dto;

import java.util.List;

public class UserDto {
    private String run;
    private String nombre;
    private String apellidos;
    private String correo;
    private String fechaNacimiento;
    private String tipoUsuario;
    private List<AddressDto> addresses;

    public UserDto() {}

    public String getRun() { return run; }
    public void setRun(String run) { this.run = run; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public List<AddressDto> getAddresses() { return addresses; }
    public void setAddresses(List<AddressDto> addresses) { this.addresses = addresses; }
}
