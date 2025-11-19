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
public class Habitacion {
    @Id
    @Column(nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHabitacion;

    @JoinColumn(name="idTipo", nullable=false)
    private TipoHabitacion idTipo;

    @Column (nullable=false)
    private Integer noches_descuento;

    @Column (nullable=false)
    private String estado;
}
