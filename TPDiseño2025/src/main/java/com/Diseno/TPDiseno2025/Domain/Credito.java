package com.Diseno.TPDiseno2025.Domain;

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
public class Credito extends MedioPago {
    @Id
    @Column(nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nroTarjeta;

    @JoinColumn(name="idMedioPago", nullable=false)
    private MedioPago medioPago;

    @Column(nullable=false)
    private Integer cuotas;

    @Column(nullable=false)
    private String nombreTitular;

    @Column(nullable=false)
    private String apellidoTitular;
}
