package com.service.controller;

import com.service.service.BlogsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogs")
public class BlogsController {

    private final BlogsService blogsService;

    public BlogsController(BlogsService blogsService) {
        this.blogsService = blogsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(blogsService.getAll());
    }
}
