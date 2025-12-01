package com.service.service;

import com.service.repository.RegionesComunasRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionesComunasService {

    private final RegionesComunasRepository repository;

    public RegionesComunasService(RegionesComunasRepository repository) {
        this.repository = repository;
    }

    public List<Object> getAll() {
        return repository.findAllRaw();
    }
}
