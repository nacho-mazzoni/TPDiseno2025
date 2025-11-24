package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Repository.EstadiaRepository;
import com.Diseno.TPDiseno2025.Repository.HabitacionRepository;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EstadiaServiceImpl implements EstadiaService {

    @Autowired
    private EstadiaRepository estadiaRepository;

    @Autowired
    private HuespedRepository huespedRepository;
    
    @Autowired
    private HabitacionRepository habitacionRepository;

    @Override
    @Transactional
    public void ocuparHabitacion(List<HabitacionDTO> habitacionesDTO, List<HuespedDTO> huespedesDTO) {
        
        // 1. Recuperar las ENTIDADES de los Huéspedes
        List<Huesped> listaHuespedesEntidad = new ArrayList<>();
        for (HuespedDTO hDto : huespedesDTO) {
            // Buscamos por DNI cada huésped seleccionado
            huespedRepository.findById(hDto.getIdHuesped())
                             .ifPresent(listaHuespedesEntidad::add);
        }

        //Bucle por cada habitación seleccionada...
        for (HabitacionDTO habDto : habitacionesDTO) {
            
            //Recuperar la entidad Habitación real
            Habitacion habitacionEntidad = habitacionRepository.findById(habDto.getIdHabitacion())
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

            // Crear la instancia
            Estadia estadiaInst = new Estadia();

            // Setear Huéspedes REVISAR
            //estadiaInst.setHuespedes(listaHuespedesEntidad);

            // d. Setear Hora CheckIn
            estadiaInst.setHoraCheckIn(LocalTime.now());

            //Setear Precio
            estadiaInst.setPrecio(1000.0);

            // Setear Habitación
            estadiaInst.setHabitacion(habitacionEntidad);
            
            // Registrar Estadía en BD
            estadiaRepository.save(estadiaInst);

            habitacionEntidad.setEstado("OCUPADA");
            habitacionRepository.save(habitacionEntidad);
        }
    }
}