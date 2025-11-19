package com.Diseno.TPDiseno2025.Domain;

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
public class Efectivo {
    @Id
    @JoinColumn(name = "id_efectivo", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEfectivo;
}
