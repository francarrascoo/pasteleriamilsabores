package com.service.pasteleriamilsabores.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.RatingDto;
import com.service.pasteleriamilsabores.dto.RatingRequest;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.models.Rating;
import com.service.pasteleriamilsabores.models.User;
import com.service.pasteleriamilsabores.repository.ProductoRepository;
import com.service.pasteleriamilsabores.repository.RatingRepository;
import com.service.pasteleriamilsabores.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository,
            ProductoRepository productoRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.productoRepository = productoRepository;
    }

    public List<RatingDto> getProductRatings(String codigoProducto) {
        if (!productoRepository.existsById(codigoProducto)) {
            throw new EntityNotFoundException("Producto no encontrado: " + codigoProducto);
        }
        return ratingRepository.findByProductoCodigoProducto(codigoProducto).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RatingDto> getUserRatings(String userRun) {
        if (!userRepository.existsById(userRun)) {
            throw new EntityNotFoundException("Usuario no encontrado: " + userRun);
        }
        return ratingRepository.findByUserRun(userRun).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Double getAverageRating(String codigoProducto) {
        if (!productoRepository.existsById(codigoProducto)) {
            throw new EntityNotFoundException("Producto no encontrado: " + codigoProducto);
        }
        List<Rating> ratings = ratingRepository.findByProductoCodigoProducto(codigoProducto);
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream()
                .mapToInt(Rating::getStars)
                .average()
                .orElse(0.0);
    }

    @Transactional
    public RatingDto createOrUpdateRating(String userRun, String codigoProducto, RatingRequest request) {
        User user = userRepository.findById(userRun)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userRun));
        Producto producto = productoRepository.findById(codigoProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + codigoProducto));

        Rating rating = ratingRepository.findByUserRunAndProductoCodigoProducto(userRun, codigoProducto)
                .orElseGet(() -> {
                    Rating newRating = new Rating();
                    newRating.setUser(user);
                    newRating.setProducto(producto);
                    return newRating;
                });

        rating.setStars(request.getStars());
        rating.setComment(request.getComment());

        Rating saved = ratingRepository.save(rating);
        return toDto(saved);
    }

    @Transactional
    public void deleteRating(String userRun, Long ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada: " + ratingId));

        if (!rating.getUser().getRun().equals(userRun)) {
            throw new IllegalArgumentException("La calificación no pertenece al usuario");
        }

        ratingRepository.deleteById(ratingId);
    }

    private RatingDto toDto(Rating entity) {
        RatingDto dto = new RatingDto();
        dto.setId(entity.getId());
        dto.setUserRun(entity.getUser().getRun());
        dto.setUserName(entity.getUser().getNombre());
        dto.setProductoCodigoProducto(entity.getProducto().getCodigoProducto());
        dto.setStars(entity.getStars());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
