package com.service.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @JsonProperty("codigo_producto")
    @Column(name = "codigo_producto")
    private String codigoProducto;
    @JsonProperty("nombre_producto")
    @Column(name = "nombre_producto")
    private String nombreProducto;
    @JsonProperty("precio_producto")
    @Column(name = "precio_producto")
    private Integer precioProducto;
    @JsonProperty("descripción_producto")
    @Column(name = "descripcion_producto")
    private String descripcionProducto;
    @JsonProperty("imagen_producto")
    @Column(name = "imagen_producto")
    private String imagenProducto;
    @Column(name = "stock")
    private Integer stock;
    @JsonProperty("stock_critico")
    @Column(name = "stock_critico")
    private Integer stockCritico;
    @Transient
    private List<Object> ratings;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public Producto() {}

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public Integer getPrecioProducto() { return precioProducto; }
    public void setPrecioProducto(Integer precioProducto) { this.precioProducto = precioProducto; }
    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }
    public String getImagenProducto() { return imagenProducto; }
    public void setImagenProducto(String imagenProducto) { this.imagenProducto = imagenProducto; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getStockCritico() { return stockCritico; }
    public void setStockCritico(Integer stockCritico) { this.stockCritico = stockCritico; }
    public List<Object> getRatings() { return ratings; }
    public void setRatings(List<Object> ratings) { this.ratings = ratings; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}
