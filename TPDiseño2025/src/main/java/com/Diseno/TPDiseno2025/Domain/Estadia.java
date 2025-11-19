package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalTime;

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
public class Estadia {
    @Id
    @Column(nullable=false, unique=true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstadia;

    @Column(nullable=false)
    private Double precio;

    @Column(nullable=false)
    private LocalTime horaCheckIn;

    @Column(nullable=false)
    private LocalTime horaCheckOut;

    @JoinColumn(name="id_reserva", referencedColumnName="id_reserva", nullable=false)
    private Reserva idReserva;
}
