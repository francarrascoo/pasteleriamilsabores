package com.service.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductosWrapper {
    @JsonProperty("nombre_pasteleria")
    private String nombrePasteleria;
    private List<Categoria> categorias;

    public ProductosWrapper() {}

    public String getNombrePasteleria() { return nombrePasteleria; }
    public void setNombrePasteleria(String nombrePasteleria) { this.nombrePasteleria = nombrePasteleria; }
    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }
}
