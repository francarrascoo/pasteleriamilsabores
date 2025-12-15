package com.service.pasteleriamilsabores.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProductoCodigoProducto(String codigoProducto);

    List<Rating> findByUserRun(String userRun);

    Optional<Rating> findByUserRunAndProductoCodigoProducto(String userRun, String codigoProducto);
}
