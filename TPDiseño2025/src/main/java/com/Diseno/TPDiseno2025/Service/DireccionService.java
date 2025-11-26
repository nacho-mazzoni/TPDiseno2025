package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;

public interface DireccionService {
    
    void crearDireccion(DireccionId id, DireccionDTO direccionDTO);
    Direccion obtenerDireccionbyId(DireccionId id);
    List<Direccion> obtenerDireccionesPorLocalidad(String localidad);
    boolean direccionExists(String calle, String numero, String localidad, String provincia, String pais);
    Direccion mapToEntDireccion(DireccionDTO dDTO);
    DireccionDTO mapToDTODireccion(Direccion d, DireccionDTO dDTO);

}
