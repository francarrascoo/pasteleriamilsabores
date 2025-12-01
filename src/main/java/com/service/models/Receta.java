package com.service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "receta")
public class Receta {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "resumen")
    private String resumen;
    @Column(name = "ingredientes")
    private String ingredientes;
    @Column(name = "preparacion")
    private String preparacion;
    @Column(name = "imagen")
    private String imagen;
    @Column(name = "badge")
    private String badge;
    @Column(name = "badge_class")
    private String badgeClass;

    public Receta() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getIngredientes() { return ingredientes; }
    public void setIngredientes(String ingredientes) { this.ingredientes = ingredientes; }
    public String getPreparacion() { return preparacion; }
    public void setPreparacion(String preparacion) { this.preparacion = preparacion; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
    public String getBadgeClass() { return badgeClass; }
    public void setBadgeClass(String badgeClass) { this.badgeClass = badgeClass; }
}
