package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Model.DireccionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;

@Service
public class HuespedServiceImp implements HuespedService {

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private HuespedRepository huespedRepository;

    @Override
    public void crearHuesped(Huesped h) {
        huespedRepository.save(h);
    }

    @Override
    public void modificarHuesped(String tipoDni, Integer numOriginal, Huesped hActualizado) {
        Huesped existente = obtenerHuesped(tipoDni, numOriginal);

        
        existente.setNombre(hActualizado.getNombre());
        existente.setApellido(hActualizado.getApellido());
        existente.setDireccion(hActualizado.getDireccion());
        

        huespedRepository.save(existente);
    }

    @Override
    public void eliminarHuesped(Huesped h) {
        huespedRepository.delete(h);
    }

    @Override
    public Huesped obtenerHuesped(String tipoDni, Integer num) {
        return huespedRepository.findByTipoDniAndNum(tipoDni, num)
                .orElse(null); // o .orElseThrow(...)
    }

    @Override
    public List<Huesped> obtenerTodos() {
        return huespedRepository.findAll();
    }

    @Override
    public void DarDeAltaHuesped(HuespedDTO dto){

        if (huespedRepository.existsByDni(dto.getDni())) {
            throw new IllegalArgumentException("El huésped ya existe");
        }

        DireccionDTO direccionDTO = dto.getDireccion();

        DireccionId id = new DireccionId();
        id.setCalle(direccionDTO.getCalle());
        id.setNumero(direccionDTO.getNumero());
        id.setDepartamento(direccionDTO.getDepartamento());
        id.setPiso(direccionDTO.getPiso());
        id.setCodPostal(direccionDTO.getCodPostal());

        Direccion direccion;

        if (direccionService.direccionExists(
                direccionDTO.getCalle(),
                direccionDTO.getNumero(),
                direccionDTO.getCiudad(),
                direccionDTO.getProvincia(),
                direccionDTO.getPais()
        )) {
            direccion = direccionService.obtenerDireccionbyId(id);
        } 
        else {
            direccionService.crearDireccion(id, direccionDTO);
            direccion = direccionService.obtenerDireccionbyId(id);
        }

        


        Huesped huesped = new Huesped();
        huesped.setNombre(dto.getNombre());
        huesped.setApellido(dto.getApellido());
        huesped.setTipoDni(dto.getTipoDni());
        huesped.setDni(dto.getDni());
        huesped.setDireccion(direccion);
        huesped.setEmail(dto.getEmail());
        huesped.setFechaNacimiento(dto.getFechaNacimiento());
        huesped.setEdad(dto.getEdad());
        huesped.setOcupacion(dto.getOcupacion());
        huesped.setPosIva(dto.getPosIva());


        huespedRepository.save(huesped);
    }
}

