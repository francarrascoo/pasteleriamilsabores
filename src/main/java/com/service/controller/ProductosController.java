package com.service.controller;

import com.service.service.ProductosService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {

    private final ProductosService productosService;

    public ProductosController(ProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(productosService.getAll());
    }
}
