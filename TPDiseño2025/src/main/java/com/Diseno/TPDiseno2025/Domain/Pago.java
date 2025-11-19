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
public class Pago {
    @Id
    @Column(nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPago;

    @JoinColumn(name="idMedioPago", nullable=false)
    private MedioPago medioPago;

    @JoinColumn(name="idFactura", nullable=false)
    private Factura factura;

    @JoinColumn(name="idResponsablePago", nullable=false)
    private ResponsablePago responsablePago;

    @Column(nullable=false)
    private Double monto;

    @Column(nullable=false)
    private LocalDate fechaPago;
}
