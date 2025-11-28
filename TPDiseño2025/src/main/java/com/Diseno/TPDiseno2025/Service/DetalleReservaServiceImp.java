package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Repository.DetalleReservaRepository;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;

@Service
public class DetalleReservaServiceImp implements DetalleReservaService {

    @Autowired
    private DetalleReservaRepository detalleReservaRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    private ReservaService reservaService = new ReservaServiceImp();
    
    private TipoHabitacionService tipoHabService = new TipoHabitacionServiceImp();

    @Override
    public void crearDetalle_Reserva(Integer idDetalle, ReservaDTO r, HabitacionDTO h) {
        DetalleReserva detalle = new DetalleReserva();
        detalle.setIdDetalle(idDetalle);
        detalle.setReserva(reservaService.mapToEntity(r));
        detalle.setHabitacion(habitacionRepository.findById(h.getIdHabitacion()).get());
        detalle.setCantidadNoches(r.getCantNoches());
        detalle.setPrecio(tipoHabService.getPrecioByTipo(h.getIdTipo()));
        detalleReservaRepository.save(detalle);
    }

    @Override
    public DetalleReserva obtenerDetalleByIdReservaAndIdHabitacion(Integer r, Integer h) {
        return detalleReservaRepository.findByReserva_IdReservaAndHabitacion_IdHabitacion(r, h);
    }

    @Override
    public DetalleReserva obtenerDetalle(Integer idDetalle) {
        return detalleReservaRepository.getById(idDetalle);
    }

    @Override
    public void eliminarDetalle(Integer idDetalle) {
        detalleReservaRepository.deleteById(idDetalle);
    }

    @Override
    public List<DetalleReserva> obtenerTodos() {
        return detalleReservaRepository.findAll();
    }

    @Override
    public void darDeBajaHabitacion(Integer idDetalle, Habitacion h) {
        detalleReservaRepository.delete(detalleReservaRepository.findById(idDetalle).get());
    }

    @Override
    public void guardarDetalle(DetalleReserva dr){
        detalleReservaRepository.save(dr);
    }

    @Override
    public List<DetalleReserva> buscarReservasEnConflicto(LocalDate fechaInicio, LocalDate fechaFin){
        return detalleReservaRepository.findReservasEnRango(fechaInicio, fechaFin);
    }
}
