package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Detalle_Reserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;

public interface Detalle_ReservaService {
    
    void crearDetalle_Reserva(Integer detalle, ReservaDTO r, HabitacionDTO h);
    void eliminarDetalle(Integer idDetalle);
    void darDeBajaHabitacion(Detalle_Reserva d, Habitacion h);
    Detalle_Reserva obtenerDetalleByIdReservaAndIdHabitacion(Integer idReserva, Integer idHabitacion);
    Detalle_Reserva obtenerDetalle(Integer idDetalle);
    List<Detalle_Reserva> obtenerTodos();

}
