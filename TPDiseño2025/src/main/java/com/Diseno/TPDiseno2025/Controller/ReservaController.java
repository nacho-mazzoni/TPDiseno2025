package com.Diseno.TPDiseno2025.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Model.CeldaCalendarioDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Service.DetalleReservaService;
import com.Diseno.TPDiseno2025.Service.HabitacionService;
import com.Diseno.TPDiseno2025.Service.HuespedService;
import com.Diseno.TPDiseno2025.Service.ReservaService;

import lombok.Data;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reservas") 
public class ReservaController {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(HuespedController.class);

    private final ReservaService reservaService;
    private final HuespedService huespedService;
    private final HabitacionService habitacionService;
    private final DetalleReservaService detalleReservaService;

    public ReservaController(final ReservaService reservaService, final HuespedService huespedService, final HabitacionService habitacionService, final DetalleReservaService detalleReservaService){
        this.reservaService = reservaService;
        this.huespedService = huespedService;
        this.habitacionService = habitacionService;
        this.detalleReservaService = detalleReservaService;
    }

    // ENDPOINT 1: CREAR RESERVA (Soporta el botón "Aceptar" del último mockup)
    @PostMapping("/crear")
    public ResponseEntity<String> crearReserva(@RequestBody SolicitudReserva request) {
        try {
            // El frontend envía un objeto grande con todo junto.
            // Desglosamos para usar tu lógica actual.
            reservaService.crearReserva(request.getReserva(), request.getHuesped(), request.getHabitacion());
            return ResponseEntity.ok("Reserva creada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear reserva: " + e.getMessage());
        }
    }

    // ENDPOINT 2: DISPONIBILIDAD (Soporta la Grilla de Colores)
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<CeldaCalendarioDTO>> obtenerDisponibilidad(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String inicioStr,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String finStr) {

        LocalDate inicio = LocalDate.parse(inicioStr);
        LocalDate fin = LocalDate.parse(finStr);
        
        List<CeldaCalendarioDTO> grilla = new ArrayList<>();
        List<HabitacionDTO> habitaciones = habitacionService.obtenerTodas().stream()
                .map(h -> habitacionService.mapToDTOHabitacion(h)).toList();

        // Obtenemos todas las reservas que impactan en este rango (Lógica que debe estar en Repo)
        List<DetalleReserva> ocupaciones = detalleReservaService.buscarReservasEnConflicto(inicio, fin); 

        // Generar la matriz Fecha x Habitacion
        for (LocalDate date = inicio; !date.isAfter(fin); date = date.plusDays(1)) {
            final LocalDate currentDate = date; // necesario para lambdas
            
            for (HabitacionDTO hab : habitaciones) {
                CeldaCalendarioDTO celda = new CeldaCalendarioDTO();
                celda.setFecha(currentDate.toString());
                celda.setIdHabitacion(hab.getIdHabitacion());

                // Lógica de cruce: ¿Está esta habitación ocupada en esta fecha?
                boolean estaOcupada = ocupaciones.stream().anyMatch(d -> 
                    d.getHabitacion().getIdHabitacion().equals(hab.getIdHabitacion()) &&
                    !currentDate.isBefore(d.getReserva().getFechaInicio()) &&
                    !currentDate.isAfter(d.getReserva().getFechaInicio().plusDays(d.getCantidadNoches()))
                );

                celda.setEstado(estaOcupada ? "OCUPADA" : "LIBRE");
                grilla.add(celda);
            }
        }

        return ResponseEntity.ok(grilla);
    }

}

// Clase auxiliar para recibir el JSON completo del frontend
@Data
class SolicitudReserva {
    private ReservaDTO reserva;
    private HuespedDTO huesped;
    private HabitacionDTO habitacion;
}