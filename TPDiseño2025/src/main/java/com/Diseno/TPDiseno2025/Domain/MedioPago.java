package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;


@Entity
@Getter
public abstract class MedioPago {
    @Id
    @Column(nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedioPago;

    @Column(nullable=false)
    private String nombreMedioPago;
}
