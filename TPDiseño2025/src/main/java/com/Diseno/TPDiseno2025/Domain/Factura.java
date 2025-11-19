package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Factura {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFactura;

    @JoinColumn(name = "id_responsablepago", referencedColumnName = "idResponsablePago", nullable = false)
    private ResponsablePago idResponsablePago;
    
    @Column(nullable = false)
    private Double precioFinal;

    @JoinColumn(name = "id_estadia", referencedColumnName = "idEstadia", nullable = false)
    private Estadia idEstadia;
    
    @Column(nullable = false)
    private Integer nroFactura;

    @Column(nullable = false)
    private String tipoFactura;

    @Column(nullable= false)
    private LocalDate fecha;
}
