package com.service.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.List;

@Repository
public class RegionesComunasRepository {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<Object> findAllRaw() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/service/data/regionesComunas/regionesComunas.json")) {
            if (is == null) throw new IllegalArgumentException("Resource not found: regionesComunas.json");
            return mapper.readValue(is, new TypeReference<List<Object>>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
