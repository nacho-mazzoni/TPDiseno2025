package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "detalle_reserva", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_habitacion", "id_reserva"})
})
public class DetalleReserva {
    
    @Id
    @Column(nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @JoinColumn(name = "id_habitacion", nullable = false)
    @NotNull
    private Habitacion idHabitacion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer cantidad;

    @JoinColumn(name = "id_reserva", nullable = false)
    @NotNull
    private Reserva idReserva;
    
}
