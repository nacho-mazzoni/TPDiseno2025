package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Detalle_Reserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Repository.Detalle_ReservaRepository;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;

@Service
public class Detalle_ReservaServiceImp implements Detalle_ReservaService {
    
    ReservaService reservaService;
    HabitacionRepository habitacionRepository;
    TipoHabitacionService tipoHabService;
    @Autowired
    private Detalle_ReservaRepository detalle_ReservaRepository;

    @Override
    public void crearDetalle_Reserva(Integer idDetalle, ReservaDTO r, HabitacionDTO h){
        Detalle_Reserva detalle = new Detalle_Reserva();
        detalle.setIdDetalle(idDetalle);
        detalle.setIdReserva(reservaService.mapToEntity(r));
        detalle.setIdHabitacion(habitacionRepository.findById(h.getIdHabitacion()).get());
        detalle.setCantidad(r.getCantNoches());
        detalle.setPrecio(tipoHabService.getPrecioByTipo(h.getIdTipo().getIdTipo()));
        detalle_ReservaRepository.save(detalle);
    }

    @Override
    public Detalle_Reserva obtenerDetalleByIdReservaAndIdHabitacion(Integer r, Integer h){
        return detalle_ReservaRepository.findByIdReservaAndIdHabitacion(r, h);
    }

    @Override
    public Detalle_Reserva obtenerDetalle(Integer idDetalle){
        return detalle_ReservaRepository.getById(idDetalle);
    }

    @Override 
    public void eliminarDetalle(Integer idDetalle){
        detalle_ReservaRepository.deleteById(idDetalle);
    }

    @Override
    public List<Detalle_Reserva> obtenerTodos(){
        return detalle_ReservaRepository.findAll();
    }

    @Override
    public void darDeBajaHabitacion(Detalle_Reserva d, Habitacion h){
        detalle_ReservaRepository.delete(d);
    }
}
