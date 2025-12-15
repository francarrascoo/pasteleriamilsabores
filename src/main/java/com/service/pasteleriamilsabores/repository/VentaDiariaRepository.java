package com.service.pasteleriamilsabores.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.pasteleriamilsabores.models.VentaDiaria;

@Repository
public interface VentaDiariaRepository extends JpaRepository<VentaDiaria, Long> {
    List<VentaDiaria> findByFecha(LocalDate fecha);
    
    List<VentaDiaria> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<VentaDiaria> findByProductoCodigoProducto(String codigoProducto);
    
    List<VentaDiaria> findByProductoCodigoProductoAndFechaBetween(String codigoProducto, LocalDate fechaInicio, LocalDate fechaFin);
    
    Optional<VentaDiaria> findByFechaAndProductoCodigoProducto(LocalDate fecha, String codigoProducto);
}
