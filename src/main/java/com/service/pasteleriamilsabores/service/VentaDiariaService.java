package com.service.pasteleriamilsabores.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.VentaDiariaDto;
import com.service.pasteleriamilsabores.models.VentaDiaria;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.repository.VentaDiariaRepository;
import com.service.pasteleriamilsabores.repository.ProductoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VentaDiariaService {

    private final VentaDiariaRepository ventaDiariaRepository;
    private final ProductoRepository productoRepository;

    public VentaDiariaService(VentaDiariaRepository ventaDiariaRepository, ProductoRepository productoRepository) {
        this.ventaDiariaRepository = ventaDiariaRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<VentaDiariaDto> listarVentasPorFecha(LocalDate fecha) {
        return ventaDiariaRepository.findByFecha(fecha).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaDiariaDto> listarVentasEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaDiariaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaDiariaDto> listarVentasProducto(String codigoProducto) {
        return ventaDiariaRepository.findByProductoCodigoProducto(codigoProducto).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VentaDiariaDto> listarVentasProductoEnRango(String codigoProducto, LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaDiariaRepository.findByProductoCodigoProductoAndFechaBetween(codigoProducto, fechaInicio, fechaFin).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VentaDiariaDto registrarVenta(LocalDate fecha, String codigoProducto, Integer cantidad, BigDecimal ingresos) {
        Producto producto = productoRepository.findById(codigoProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + codigoProducto));

        VentaDiaria venta = ventaDiariaRepository.findByFechaAndProductoCodigoProducto(fecha, codigoProducto)
                .orElse(null);

        if (venta == null) {
            // crear nueva venta diaria
            venta = new VentaDiaria();
            venta.setFecha(fecha);
            venta.setProducto(producto);
            venta.setCantidadVendida(cantidad);
            venta.setIngresosTotal(ingresos);
        } else {
            // actualizar venta existente (sumar cantidad e ingresos)
            venta.setCantidadVendida(venta.getCantidadVendida() + cantidad);
            venta.setIngresosTotal(venta.getIngresosTotal().add(ingresos));
        }

        return toDto(ventaDiariaRepository.save(venta));
    }

    private VentaDiariaDto toDto(VentaDiaria venta) {
        VentaDiariaDto dto = new VentaDiariaDto();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        if (venta.getProducto() != null) {
            dto.setProductoCodiogo(venta.getProducto().getCodigoProducto());
            dto.setProductoNombre(venta.getProducto().getNombreProducto());
        }
        dto.setCantidadVendida(venta.getCantidadVendida());
        dto.setIngresosTotal(venta.getIngresosTotal());
        return dto;
    }
}
