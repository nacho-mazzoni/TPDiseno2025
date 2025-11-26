package com.Diseno.TPDiseno2025.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;

@Repository
public interface DetalleReservaRepository extends JpaRepository<DetalleReserva, Integer> {
    @Override
    Optional<DetalleReserva> findById(Integer idDetalle);

    Boolean existsByHabitacion_IdHabitacionAndReserva_IdReserva(Integer idHabitacion, Integer idReserva);

    List<DetalleReserva> findByReserva_IdReserva(Integer idReserva);

    @Override
    DetalleReserva getById(Integer idDetalle);

    @Override
    void deleteById(Integer idDetalle);

    DetalleReserva findByReserva_IdReservaAndHabitacion_IdHabitacion(Integer idReserva, Integer IdHabitacion);
}
