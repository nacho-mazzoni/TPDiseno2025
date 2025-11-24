package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionEstadosDTO;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;



public class HabitacionServiceImp implements HabitacionService{

    @Autowired
    private HabitacionRepository habitacionRepository;

    private TipoHabitacionService tipoHabitacionService;

    @Autowired
    private ReservaService reservaService;

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
    public Optional<Habitacion> obtenerHabitacionPorId(Integer idhabitacion){
        return habitacionRepository.findById(idhabitacion);
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
    public List<HabitacionEstadosDTO> mostrarestadoHabitacionesByFecha(LocalDate fechaDesde, LocalDate fechaHasta){
        
        List<Habitacion> habitaciones = this.obtenerTodas();

        List<HabitacionEstadosDTO> matrizHabitacionesEstados = new ArrayList<>();

        for (Habitacion h : habitaciones) {
            HabitacionEstadosDTO dto = new HabitacionEstadosDTO();
            dto.setIdHabitacion(h.getIdHabitacion());
            dto.setIdTipo(h.getIdTipo());
            
            List<String> estadosPorDia = new ArrayList<>();
            LocalDate fechaActual = fechaDesde;

            while (!fechaActual.isAfter(fechaHasta)) {
                // Usamos la función que devuelve el estado por día
                String estadoDia = reservaService.habitacionReservadaPorDia(h, fechaActual);
                estadosPorDia.add(estadoDia);
                fechaActual = fechaActual.plusDays(1);
            }

            dto.setEstados(estadosPorDia);
            matrizHabitacionesEstados.add(dto);
    }

    return matrizHabitacionesEstados;
    }
}
