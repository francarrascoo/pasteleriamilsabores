package com.service.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_categoria")
    @Column(name = "id_categoria")
    private Integer idCategoria;
    @JsonProperty("nombre_categoria")
    @Column(name = "nombre_categoria")
    private String nombreCategoria;
    @OneToMany(mappedBy = "categoria", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private final List<Producto> productos = new ArrayList<>();

    public Categoria() {}

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) {
        this.productos.clear();
        if (productos != null) {
            for (Producto p : productos) {
                p.setCategoria(this);
                this.productos.add(p);
            }
        }
    }
    public void addProducto(Producto producto) {
        producto.setCategoria(this);
        this.productos.add(producto);
    }
    public void removeProducto(Producto producto) {
        producto.setCategoria(null);
        this.productos.remove(producto);
    }
}
