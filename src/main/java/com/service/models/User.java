package com.service.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "run")
    private String run;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "correo")
    private String correo;
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    @Column(name = "tipo_usuario")
    private String tipoUsuario;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Address> addresses = new ArrayList<>();
    @Column(name = "password")
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
    public void setAddresses(List<Address> addresses) {
        this.addresses.clear();
        if (addresses != null) {
            for (Address a : addresses) {
                a.setUser(this);
                this.addresses.add(a);
            }
        }
    }
    public void addAddress(Address address) {
        address.setUser(this);
        this.addresses.add(address);
    }
    public void removeAddress(Address address) {
        address.setUser(null);
        this.addresses.remove(address);
    }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
