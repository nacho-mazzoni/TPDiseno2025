package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
        hDTO.setIdTipo(h.getIdTipo().getIdTipo());
        hDTO.setNochesDescuento(h.getNochesDescuento());
        hDTO.setEstado(h.getEstado());
        return hDTO;
    }

    @Override
    public Habitacion mapToEntHabitacion(HabitacionDTO hDTO){
        Habitacion h = new Habitacion();
        h.setIdHabitacion(hDTO.getIdHabitacion());
        h.setIdTipo(tipoHabitacionService.getTipoByIdTipo(hDTO.getIdTipo()));
        h.setNochesDescuento(hDTO.getNochesDescuento());
        h.setEstado(hDTO.getEstado());

        return h;
    }

    @Override
    public List<Habitacion> obtenerTodas(){
        return habitacionRepository.findAll();
    }
    
    @Override
    public List<Habitacion> mostrarestadoHabitacionesByFecha(String fechaDesde, String fechaHasta){
        return this.obtenerTodas();
    }
}
