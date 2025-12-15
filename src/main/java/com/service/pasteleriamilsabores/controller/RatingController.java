package com.service.pasteleriamilsabores.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.dto.RatingDto;
import com.service.pasteleriamilsabores.dto.RatingRequest;
import com.service.pasteleriamilsabores.service.RatingService;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping(path = "/producto/{codigoProducto}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatingDto>> getProductRatings(@PathVariable String codigoProducto) {
        try {
            return ResponseEntity.ok(ratingService.getProductRatings(codigoProducto));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/producto/{codigoProducto}/average", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable String codigoProducto) {
        try {
            Double average = ratingService.getAverageRating(codigoProducto);
            int totalRatings = ratingService.getProductRatings(codigoProducto).size();
            return ResponseEntity.ok(Map.of(
                    "codigoProducto", codigoProducto,
                    "promedio", average,
                    "totalCalificaciones", totalRatings));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(path = "/usuario/{userRun}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatingDto>> getUserRatings(@PathVariable String userRun) {
        try {
            return ResponseEntity.ok(ratingService.getUserRatings(userRun));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(path = "/usuario/{userRun}/producto/{codigoProducto}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatingDto> createOrUpdateRating(@PathVariable String userRun,
            @PathVariable String codigoProducto, @RequestBody RatingRequest request) {
        try {
            if (request.getStars() == null || request.getStars() < 1 || request.getStars() > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            RatingDto rating = ratingService.createOrUpdateRating(userRun, codigoProducto, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(rating);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(path = "/usuario/{userRun}/producto/{codigoProducto}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatingDto> updateRating(@PathVariable String userRun,
            @PathVariable String codigoProducto, @RequestBody RatingRequest request) {
        try {
            if (request.getStars() == null || request.getStars() < 1 || request.getStars() > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            RatingDto rating = ratingService.createOrUpdateRating(userRun, codigoProducto, request);
            return ResponseEntity.ok(rating);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{ratingId}/usuario/{userRun}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId, @PathVariable String userRun) {
        try {
            ratingService.deleteRating(userRun, ratingId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
