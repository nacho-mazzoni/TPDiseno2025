package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Repository.DireccionRepository;

@Service
public class DireccionServiceImp implements DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;

    @Override
    public void crearDireccion(DireccionId id, DireccionDTO direccionDTO) {
        Direccion direccion = new Direccion();
        direccion.setId(id);
        direccion.setLocalidad(direccionDTO.getLocalidad());
        direccion.setProvincia(direccionDTO.getProvincia());
        direccion.setPais(direccionDTO.getPais());
        direccionRepository.save(direccion);
    }

    @Override
    public Direccion obtenerDireccionbyId(DireccionId id) {
        return direccionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Direccion> obtenerDireccionesPorCiudad(String ciudad) {
        return direccionRepository.findByCiudadStartingWithIgnoreCase(ciudad);
    }

    @Override
    public boolean direccionExists(String calle, String numero, String ciudad, String provincia, String pais) {
        return direccionRepository.existsByCalleAndNumeroAndCiudadAndProvinciaAndPais(calle, numero, ciudad, provincia, pais);
    }

}
