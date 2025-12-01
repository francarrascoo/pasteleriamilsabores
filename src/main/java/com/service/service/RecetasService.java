package com.service.service;

import com.service.repository.RecetasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecetasService {

    private final RecetasRepository repository;

    public RecetasService(RecetasRepository repository) {
        this.repository = repository;
    }

    public List<Object> getAll() {
        return repository.findAllRaw();
    }
}
