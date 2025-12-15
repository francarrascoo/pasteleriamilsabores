package com.service.pasteleriamilsabores.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.ProductoDto;
import com.service.pasteleriamilsabores.models.Categoria;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.repository.CategoriaRepository;
import com.service.pasteleriamilsabores.repository.ProductoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductosService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductosService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> listarProductos() {
        return productoRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductoDto buscarPorCodigo(String codigoProducto) {
        return productoRepository.findById(codigoProducto)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con codigo: " + codigoProducto));
    }

    @Transactional
    public ProductoDto crearProducto(ProductoDto dto) {
        if (dto.getCodigoProducto() == null || dto.getCodigoProducto().isBlank()) {
            throw new IllegalArgumentException("Codigo de producto requerido");
        }
        if (productoRepository.existsById(dto.getCodigoProducto())) {
            throw new IllegalArgumentException("Producto ya existe con codigo: " + dto.getCodigoProducto());
        }
        Producto entity = fromDto(dto);
        return toDto(productoRepository.save(entity));
    }

    @Transactional
    public ProductoDto actualizarProducto(String codigoProducto, ProductoDto dto) {
        Producto existing = productoRepository.findById(codigoProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con codigo: " + codigoProducto));
        applyDto(existing, dto);
        existing.setCodigoProducto(codigoProducto);
        return toDto(productoRepository.save(existing));
    }

    @Transactional
    public void eliminarProducto(String codigoProducto) {
        Producto existing = productoRepository.findById(codigoProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con codigo: " + codigoProducto));
        productoRepository.delete(existing);
    }

    private ProductoDto toDto(Producto producto) {
        ProductoDto dto = new ProductoDto();
        dto.setCodigoProducto(producto.getCodigoProducto());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setPrecioProducto(producto.getPrecioProducto());
        dto.setDescripcionProducto(producto.getDescripcionProducto());
        dto.setImagenProducto(producto.getImagenProducto());
        dto.setStock(producto.getStock());
        dto.setStockCritico(producto.getStockCritico());
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getIdCategoria());
            dto.setCategoriaNombre(producto.getCategoria().getNombreCategoria());
        }
        return dto;
    }

    private Producto fromDto(ProductoDto dto) {
        Producto producto = new Producto();
        applyDto(producto, dto);
        producto.setCodigoProducto(dto.getCodigoProducto());
        return producto;
    }

    private void applyDto(Producto producto, ProductoDto dto) {
        if (dto.getNombreProducto() != null) {
            producto.setNombreProducto(dto.getNombreProducto());
        }
        producto.setPrecioProducto(dto.getPrecioProducto());
        producto.setDescripcionProducto(dto.getDescripcionProducto());
        producto.setImagenProducto(dto.getImagenProducto());
        producto.setStock(dto.getStock());
        producto.setStockCritico(dto.getStockCritico());
        Categoria categoria = resolveCategoria(dto);
        if (categoria != null) {
            producto.setCategoria(categoria);
        }
    }

    /**
     * Resolve la categoria por ID o por nombre; si no existe y viene el nombre,
     * la crea automáticamente. Devuelve null si no se proporcionó ni ID ni nombre.
     */
    private Categoria resolveCategoria(ProductoDto dto) {
        if (dto.getCategoriaId() != null) {
            return categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + dto.getCategoriaId()));
        }
        String nombreCategoria = dto.getCategoriaNombre();
        if (nombreCategoria == null || nombreCategoria.isBlank()) {
            return null;
        }
        return categoriaRepository.findByNombreCategoriaIgnoreCase(nombreCategoria)
                .orElseGet(() -> {
                    Categoria nueva = new Categoria();
                    nueva.setNombreCategoria(nombreCategoria.trim());
                    return categoriaRepository.save(nueva);
                });
    }
}
