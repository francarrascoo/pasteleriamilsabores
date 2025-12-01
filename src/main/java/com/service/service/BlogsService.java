package com.service.service;

import com.service.repository.BlogsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogsService {

    private final BlogsRepository repository;

    public BlogsService(BlogsRepository repository) {
        this.repository = repository;
    }

    public List<Object> getAll() {
        return repository.findAllRaw();
    }
}
