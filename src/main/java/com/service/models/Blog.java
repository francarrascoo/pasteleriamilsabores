package com.service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "resumen")
    private String resumen;
    @Column(name = "imagen")
    private String imagen;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "autor")
    private String autor;
    @Column(name = "link")
    private String link;

    public Blog() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
}
