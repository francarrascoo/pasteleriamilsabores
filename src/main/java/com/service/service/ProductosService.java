package com.service.service;

import com.service.repository.ProductosRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductosService {

    private final ProductosRepository repository;

    public ProductosService(ProductosRepository repository) {
        this.repository = repository;
    }

    public Object getAll() {
        return repository.findAllRaw();
    }
}
