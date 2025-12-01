package com.service.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
public class ProductosRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    public Object findAllRaw() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/service/data/products/productos.json")) {
            if (is == null) throw new IllegalArgumentException("Resource not found: productos.json");
            return mapper.readValue(is, Object.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
