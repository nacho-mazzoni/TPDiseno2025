package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;

public interface DetalleReservaService {
    
    void crearDetalle_Reserva(Integer detalle, ReservaDTO r, HabitacionDTO h);
    void eliminarDetalle(Integer idDetalle);
    void darDeBajaHabitacion(Integer idDetalle, Habitacion h);
    DetalleReserva obtenerDetalleByIdReservaAndIdHabitacion(Integer idReserva, Integer idHabitacion);
    DetalleReserva obtenerDetalle(Integer idDetalle);
    List<DetalleReserva> obtenerTodos();

}
