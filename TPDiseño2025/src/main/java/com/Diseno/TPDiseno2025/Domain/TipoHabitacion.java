package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class TipoHabitacion {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipo;

    @Column(nullable = false)
    private String nombreTipo;

    @Column(nullable = false)
    private Double precioNoche;

    @Column(nullable = false)
    private Integer cantidadDisponible;


    public TipoHabitacion(){}

    public TipoHabitacion(Integer idTipo, String nombreTipo){
        this.idTipo = idTipo;
        this.nombreTipo = nombreTipo;
    }
}