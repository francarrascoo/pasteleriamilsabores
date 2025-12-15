package com.service.pasteleriamilsabores.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.dto.VentaDiariaDto;
import com.service.pasteleriamilsabores.service.VentaDiariaService;

@RestController
@RequestMapping("/api/ventas-diarias")
public class VentaDiariaController {

    private final VentaDiariaService ventaDiariaService;

    public VentaDiariaController(VentaDiariaService ventaDiariaService) {
        this.ventaDiariaService = ventaDiariaService;
    }

    @GetMapping(path = "/fecha/{fecha}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VentaDiariaDto>> getVentasPorFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ventaDiariaService.listarVentasPorFecha(fecha));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VentaDiariaDto>> getVentasEnRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(ventaDiariaService.listarVentasEnRango(fechaInicio, fechaFin));
    }

    @GetMapping(path = "/producto/{codigoProducto}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VentaDiariaDto>> getVentasProducto(@PathVariable String codigoProducto) {
        return ResponseEntity.ok(ventaDiariaService.listarVentasProducto(codigoProducto));
    }

    @GetMapping(path = "/producto/{codigoProducto}/rango", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VentaDiariaDto>> getVentasProductoEnRango(
            @PathVariable String codigoProducto,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(ventaDiariaService.listarVentasProductoEnRango(codigoProducto, fechaInicio, fechaFin));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VentaDiariaDto> registrarVenta(@RequestBody VentaDiariaRegistroRequest request) {
        try {
            VentaDiariaDto venta = ventaDiariaService.registrarVenta(request.getFecha(), request.getProductoCodiogo(), 
                    request.getCantidadVendida(), request.getIngresosTotal());
            return ResponseEntity.status(HttpStatus.CREATED).body(venta);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public static class VentaDiariaRegistroRequest {
        private LocalDate fecha;
        private String productoCodiogo;
        private Integer cantidadVendida;
        private java.math.BigDecimal ingresosTotal;

        public LocalDate getFecha() { return fecha; }
        public void setFecha(LocalDate fecha) { this.fecha = fecha; }

        public String getProductoCodiogo() { return productoCodiogo; }
        public void setProductoCodiogo(String productoCodiogo) { this.productoCodiogo = productoCodiogo; }

        public Integer getCantidadVendida() { return cantidadVendida; }
        public void setCantidadVendida(Integer cantidadVendida) { this.cantidadVendida = cantidadVendida; }

        public java.math.BigDecimal getIngresosTotal() { return ingresosTotal; }
        public void setIngresosTotal(java.math.BigDecimal ingresosTotal) { this.ingresosTotal = ingresosTotal; }
    }
}
