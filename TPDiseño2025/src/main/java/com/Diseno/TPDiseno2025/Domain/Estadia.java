package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Estadia {
    @Id
    @Column(name = "id_estadia", nullable=false, unique=true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEstadia;

    @Column(name = "precio", nullable=false)
    private Double precio;

    @Column(name = "hora_checkin", nullable=false)
    private LocalTime horaCheckIn;

    @Column(name = "hora_checkout", nullable=false)
    private LocalTime horaCheckOut;

    @OneToOne
    @JoinColumn(name="id_reserva", referencedColumnName="id_reserva", nullable=false)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_habitacion", nullable = false) 
    private Habitacion habitacion;

    @ManyToMany
    @JoinTable(
        name = "estadia_huespedes",
        joinColumns = @JoinColumn(name = "id_estadia"),
        inverseJoinColumns = @JoinColumn(name = "id_huesped")
    )
    private List<Huesped> huespedes;
}
