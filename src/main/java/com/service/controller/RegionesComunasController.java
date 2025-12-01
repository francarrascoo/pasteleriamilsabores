package com.service.controller;

import com.service.service.RegionesComunasService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regiones-comunas")
public class RegionesComunasController {

    private final RegionesComunasService regionesComunasService;

    public RegionesComunasController(RegionesComunasService regionesComunasService) {
        this.regionesComunasService = regionesComunasService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(regionesComunasService.getAll());
    }
}
