package com.service.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Categoria {
    @JsonProperty("id_categoria")
    private Integer idCategoria;
    @JsonProperty("nombre_categoria")
    private String nombreCategoria;
    private List<Producto> productos;

    public Categoria() {}

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    public List<Producto> getProductos() { return productos; }
    public void setProductos(List<Producto> productos) { this.productos = productos; }
}
