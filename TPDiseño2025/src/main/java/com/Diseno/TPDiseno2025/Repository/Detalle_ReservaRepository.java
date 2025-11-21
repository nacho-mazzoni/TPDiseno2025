package com.Diseno.TPDiseno2025.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Diseno.TPDiseno2025.Domain.Detalle_Reserva;

public interface Detalle_ReservaRepository extends JpaRepository<Detalle_Reserva, Integer> {
    @Override
    Optional<Detalle_Reserva> findById(Integer idDetalle);

    Boolean existsByIdHabitacionAndIdReserva(Integer idHabitacion, Integer idReserva);

    List<Detalle_Reserva> findByIdReserva(Integer idReserva);

    @Override
    Detalle_Reserva getById(Integer idDetalle);

    @Override
    void deleteById(Integer idDetalle);

    Detalle_Reserva findByIdReservaAndIdHabitacion(Integer idReserva, Integer IdHabitacion);
}
