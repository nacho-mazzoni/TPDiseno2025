package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;

public interface ReservaService {
    
    void crearReserva(Reserva r, Huesped h, Habitacion habitacion);
    void modificarReserva(Integer idReserva, Reserva rActualizada);
    void eliminarReserva(Reserva r);
    Reserva obtenerReservaPorId(Integer id);
    List<Reserva> obtenerTodas();
    Reserva mapToEntity(ReservaDTO r);
    ReservaDTO mapToDTO(Reserva r);
    List<Reserva> ObtenerReservasPorHabitacion(Habitacion habitacion);
    String habitacionReservadaPorDia(Habitacion habitacion, LocalDate fecha);
}
