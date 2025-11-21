package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;

public interface HabitacionService {
    
    void crearHabitacion(Integer idHabitacion, Integer idTipo, Integer nochesDesc, String estado);
    HabitacionDTO mapToDTOHabitacion(Habitacion h);
    Habitacion mapToEntHabitacion(HabitacionDTO hDto);
    List<Habitacion> obtenerTodas();
    List<Habitacion> mostrarestadoHabitacionesByFecha(String fechaDesde, String fechaHasta);

}
