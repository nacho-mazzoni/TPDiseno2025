package com.Diseno.TPDiseno2025.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Esta query busca todos los detalles de reservas activas que chocan con las fechas solicitadas
    @Query("SELECT d FROM DetalleReserva d " +
           "JOIN d.reserva r " +
           "WHERE r.estado != 'CANCELADA' " +
           "AND (" +
           "   (r.fechaInicio <= :fechaFin AND r.fechaInicio >= :fechaInicio) OR " +
           "   (r.fechaInicio <= :fechaInicio AND DATE_ADD(r.fechaInicio, r.cantNoches) >= :fechaInicio)" +
           ")")
    List<DetalleReserva> findReservasEnRango(@Param("fechaInicio") LocalDate fechaInicio, 
                                             @Param("fechaFin") LocalDate fechaFin);

}
