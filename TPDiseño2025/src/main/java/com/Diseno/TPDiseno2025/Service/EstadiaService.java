package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import java.util.List;

public interface EstadiaService {

    void ocuparHabitacion(List<HabitacionDTO> habitacionesDTO, List<HuespedDTO> huespedesDTO);

}