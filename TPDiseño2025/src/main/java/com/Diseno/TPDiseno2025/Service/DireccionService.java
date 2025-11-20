package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;

public interface DireccionService {
    
    void crearDireccion(DireccionId id, DireccionDTO direccionDTO);
    Direccion obtenerDireccionbyId(DireccionId id);
    List<Direccion> obtenerDireccionesPorCiudad(String ciudad);
    boolean direccionExists(String calle, String numero, String ciudad, String provincia, String pais);

}
