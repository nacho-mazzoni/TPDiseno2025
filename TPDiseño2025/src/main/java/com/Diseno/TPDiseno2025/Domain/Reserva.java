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
public class Reserva {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;

    @JoinColumn(name = "dni_huesped", referencedColumnName = "dni", nullable = false)
    private Huesped huesped;

    @Column(nullable = false)
    private Integer cantHuesped;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private Integer cantNoches;

    @JoinColumn(name = "id_habitacion", referencedColumnName = "idHabitacion", nullable = false)
    private Habitacion habitacion;

    @Column(nullable = false)
    private Boolean descuento;

    @Column
    private Estadia estadia;

    @Column(nullable=false)
    private String estado; // Aceptado, Rechazado, En Proceso, Finalizado, Pagado, Cancelado
}
