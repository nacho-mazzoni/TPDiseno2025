package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;

public interface ReservaService {
    
    void crearReserva(Reserva r, Huesped h, Habitacion habitacion);
    void modificarReserva(Integer idReserva, Reserva rActualizada);
    void eliminarReserva(Reserva r);
    Reserva obtenerReservaPorId(Integer id);
    List<Reserva> obtenerTodas();

}
