package com.service.service;

import com.service.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository repository;

    public UsersService(UsersRepository repository) {
        this.repository = repository;
    }

    public List<Object> getAll() {
        return repository.findAllRaw();
    }
}
