package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;



public class HabitacionServiceImp implements HabitacionService{

    @Autowired
    private HabitacionRepository habitacionRepository;

    private TipoHabitacionService tipoHabitacionService;

    @Override
    public void crearHabitacion(Integer idHabitacion, Integer idTipo, Integer nochesDesc, String estado){
        Habitacion nuevaHab = new Habitacion();
        nuevaHab.setIdHabitacion(idHabitacion);
        nuevaHab.setIdTipo(tipoHabitacionService.getTipoByIdTipo(idTipo));
        nuevaHab.setNochesDescuento(nochesDesc);
        nuevaHab.setEstado(estado);

        habitacionRepository.save(nuevaHab);
    }

    @Override
    public HabitacionDTO mapToDTOHabitacion(Habitacion h){
        HabitacionDTO hDTO = new HabitacionDTO();
        hDTO.setIdHabitacion(h.getIdHabitacion());
        hDTO.setIdTipo(tipoHabitacionService.mapToDTOTipoHabitacion(h.getIdTipo()));
        hDTO.setNochesDescuento(h.getNochesDescuento());
        hDTO.setEstado(h.getEstado());
    }

    @Override
    public Habitacion mapToEntHabitacion(HabitacionDTO hDTO){
        Habitacion h = new Habitacion();
        h.setIdHabitacion(hDTO.getIdHabitacion());
        h.setIdTipo(tipoHabitacionService.getTipoByIdTipoDTO(hDTO.getIdTipo()));
        h.setNochesDescuento(hDTO.getNochesDescuento());
        h.setEstado(hDTO.getEstado());

        return h;
    }

    @Override
    public List<Habitacion> obtenerTodas(){
        return habitacionRepository.findAll();
    }
    
    @Override
    List<Habitacion> mostrarestadoHabitacionesByFecha(String fechaDesde, String fechaHata){
        this.obtenerTodas();
    }
}
