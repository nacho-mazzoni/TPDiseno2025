package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;

public interface TipoHabitacionService {
    
    Double getPrecioByTipo(Integer idTipo);
    TipoHabitacion getTipoByIdTipo(Integer idTipo);

}
