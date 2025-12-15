package com.service.pasteleriamilsabores.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.CategoriaDto;
import com.service.pasteleriamilsabores.models.Categoria;
import com.service.pasteleriamilsabores.repository.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriasService {

    private final CategoriaRepository categoriaRepository;

    public CategoriasService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDto> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDto buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada con id: " + id));
    }

    @Transactional
    public CategoriaDto crearCategoria(CategoriaDto dto) {
        if (dto.getNombreCategoria() == null || dto.getNombreCategoria().isBlank()) {
            throw new IllegalArgumentException("Nombre de categoria requerido");
        }
        Categoria entity = new Categoria();
        entity.setNombreCategoria(dto.getNombreCategoria());
        return toDto(categoriaRepository.save(entity));
    }

    @Transactional
    public CategoriaDto actualizarCategoria(Integer id, CategoriaDto dto) {
        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada con id: " + id));
        if (dto.getNombreCategoria() != null && !dto.getNombreCategoria().isBlank()) {
            existing.setNombreCategoria(dto.getNombreCategoria());
        }
        return toDto(categoriaRepository.save(existing));
    }

    @Transactional
    public void eliminarCategoria(Integer id) {
        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada con id: " + id));
        categoriaRepository.delete(existing);
    }

    private CategoriaDto toDto(Categoria categoria) {
        CategoriaDto dto = new CategoriaDto();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombreCategoria(categoria.getNombreCategoria());
        return dto;
    }
}
