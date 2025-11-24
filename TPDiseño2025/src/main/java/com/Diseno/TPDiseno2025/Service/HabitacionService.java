package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionEstadosDTO;

public interface HabitacionService {
    
    void crearHabitacion(Integer idHabitacion, Integer idTipo, Integer nochesDesc, String estado);
    Optional<Habitacion>  obtenerHabitacionPorId(Integer idhabitacion);
    HabitacionDTO mapToDTOHabitacion(Habitacion h);
    Habitacion mapToEntHabitacion(HabitacionDTO hDto);
    List<Habitacion> obtenerTodas();
    List<HabitacionEstadosDTO> mostrarestadoHabitacionesByFecha(LocalDate fechaDesde, LocalDate fechaHasta);

}
