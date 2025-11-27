package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.*;
import com.Diseno.TPDiseno2025.Model.OcuparHabitacionRequest;
import com.Diseno.TPDiseno2025.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
public class EstadiaService {

    @Autowired
    private EstadiaRepository estadiaDAO; // En el diagrama: EstadiaDAO

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private HuespedRepository huespedRepository;

    @Transactional
    public void ocuparHabitacion(OcuparHabitacionRequest request) {
        
        // 1. Recuperamos las listas reales de la BD usando los IDs del request
        List<Habitacion> listaHabitaciones = habitacionRepository.findAllById(request.getIdsHabitaciones());
        List<Huesped> listaHuespedes = huespedRepository.findAllById(request.getIdsHuespedes());

        // --- INICIO DEL BUCLE DEL DIAGRAMA ---
        // "loop [por cada habitacion en ListaHabitaciones]"
        for (Habitacion habitacion : listaHabitaciones) {
            
            // "<create>" -> Crear instancia
            Estadia estadiaInst = new Estadia();

            // "setHuespedes(List<HuespedDTO>...)" -> Asignamos los huéspedes
            estadiaInst.setHuespedes(listaHuespedes);

            // "setHoraCheckIn(hora_CheckIn)" -> Usamos la hora actual
            estadiaInst.setHoraCheckIn(LocalTime.now());

            // "setPrecio(Precio)"
            estadiaInst.setPrecio(request.getPrecio());

            // "setHabitacion(Habitacion)" -> Asignamos la habitación del bucle
            estadiaInst.setHabitacion(habitacion);

            // "registrarEstadia(EstadiaInst)" -> Guardar en BD
            estadiaDAO.save(estadiaInst);

            // Adicional (Lógica de GestorHabitacion): Marcar la habitación como Ocupada
            habitacion.setEstado("Ocupada");
            habitacionRepository.save(habitacion);
        }
        // --- FIN DEL BUCLE ---
    }
}
