package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;
import com.Diseno.TPDiseno2025.Repository.ReservaRepository;

@Service
public class ReservaServiceImp implements ReservaService {
    
    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public void crearReserva(Reserva r, Huesped h, Habitacion habitacion) {
        
        r.setHuesped(h);
        r.setHabitacion(habitacion);
        reservaRepository.save(r);
    }

    @Override
    public void modificarReserva(Integer idReserva, Reserva rActualizada) {
        Reserva existente = obtenerReservaPorId(idReserva);
        
        existente.setCantHuesped(rActualizada.getCantHuesped());
        existente.setFechaInicio(rActualizada.getFechaInicio());
        existente.setCantNoches(rActualizada.getCantNoches());
        
        reservaRepository.save(existente);
    }

    @Override
    public void eliminarReserva(Reserva r) {    
        reservaRepository.delete(r);
    }

    @Override
    public Reserva obtenerReservaPorId(Integer id) {    
        return reservaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reserva> obtenerTodas() {    
        return reservaRepository.findAll();
    }

    @Override
    public Reserva mapToEntity(ReservaDTO r){
        Reserva reserva = new Reserva();
        reserva.setIdReserva(r.getIdReserva());
        reserva.setHuesped(huespedRepository.findById(r.getIdHuesped()).get());
        reserva.setCantHuesped(r.getCantHuesped());
        reserva.setFechaInicio(LocalDate.parse(r.getFechaInicio()));
        reserva.setCantNoches(r.getCantNoches());
        reserva.setDescuento(r.getDescuento());
        reserva.setHabitacion(habitacionRepository.findById(r.getIdHabitacion()).get());
        reserva.setEstado(r.getEstado());

        return reserva;
    }   
    
    @Override
    public ReservaDTO mapToDTO(Reserva r){
        ReservaDTO reserva = new ReservaDTO();
        reserva.setIdReserva(r.getIdReserva());
        reserva.setIdHuesped(r.getHuesped().getDni());
        reserva.setCantHuesped(r.getCantHuesped());
        reserva.setFechaInicio(r.getFechaInicio().toString());
        reserva.setCantNoches(r.getCantNoches());
        reserva.setDescuento(r.getDescuento());
        reserva.setIdHabitacion(r.getHabitacion().getIdHabitacion());
        reserva.setEstado(r.getEstado());

        return reserva;
    }   

    public List<Reserva> obtenerReservasPorHabitacion(Habitacion habitacion) {
        return reservaRepository.findByHabitacion(habitacion);
    }

    public String habitacionReservadaPorDia(Habitacion habitacion, LocalDate fecha){

        String estado = "libre";
        List<Reserva> reservas = this.obtenerReservasPorHabitacion(habitacion);

        for (Reserva r : reservas) {
            LocalDate inicioReserva = r.getFechaInicio();
            LocalDate finReserva = inicioReserva.plusDays(r.getCantNoches() - 1);

            if (!fecha.isBefore(inicioReserva) && !fecha.isAfter(finReserva)) {
                if (r.getEstadia() != null) {
                    estado = "Ocupado";
                } else {
                    estado = "Reservado";
                }
            }
        }
        return estado;
    }
}
