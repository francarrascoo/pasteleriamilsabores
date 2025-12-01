package com.service.models;

import java.util.List;

public class User {
    private String run;
    private String nombre;
    private String apellidos;
    private String correo;
    private String fechaNacimiento;
    private String tipoUsuario;
    private List<Address> addresses;
    private String password;

    public User() {}

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
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
