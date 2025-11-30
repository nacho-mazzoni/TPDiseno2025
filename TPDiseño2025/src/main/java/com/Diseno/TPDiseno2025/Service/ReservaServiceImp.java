package com.Diseno.TPDiseno2025.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.DetalleReserva;
import com.Diseno.TPDiseno2025.Domain.Habitacion;
import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Domain.Reserva;
import com.Diseno.TPDiseno2025.Model.CeldaCalendarioDTO;
import com.Diseno.TPDiseno2025.Model.HabitacionDTO;
import com.Diseno.TPDiseno2025.Model.HuespedDTO;
import com.Diseno.TPDiseno2025.Model.ReservaDTO;
import com.Diseno.TPDiseno2025.Repository.ReservaRepository;



@Service
public class ReservaServiceImp implements ReservaService {
    
    @Autowired
    private HuespedService huespedService;
    
    @Autowired
    private HabitacionService habitacionService;
    
    
    @Autowired
    private DetalleReservaService detalleService;

    @Autowired
    private ReservaRepository reservaRepository;

    @Override   
    public void crearReserva(ReservaDTO r, HuespedDTO h, HabitacionDTO habitacion) {
        
        // 1. Buscamos al huésped. 
        // Como tu HuespedService lanza 'NotFoundException' si no lo encuentra, 
        // esto cortará el flujo automáticamente con un error 404 si el DNI no existe.
        Huesped huespedExistente = huespedService.buscarHuespedByTipoDniAndDni(h.getTipoDni(), h.getDni());

        // 2. Mapeamos la reserva
        Reserva nuevaReserva = this.mapToEntity(r);
        
        // 3. Asignamos el huésped REAL que trajimos de la BD
        nuevaReserva.setHuesped(huespedExistente);

        // 4. Guardamos la Reserva
        reservaRepository.save(nuevaReserva);

        // 5. Guardamos el Detalle
        DetalleReserva detalle = new DetalleReserva();
        detalle.setReserva(nuevaReserva);
    
        // Buscamos la habitación real para asegurar consistencia
        Habitacion habReal = habitacionService.buscarHabitacionByIdHabitacion(habitacion.getIdHabitacion());
        detalle.setHabitacion(habReal);
    
        // Precio y Noches
        detalle.setPrecio(habReal.getIdTipo().getPrecioNoche() * r.getCantNoches()); 
        detalle.setCantidadNoches(r.getCantNoches());

        // Guardamos el detalle sin dependencias circulares
        detalleService.guardarDetalle(detalle);
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
        //reserva.setHuesped(huespedService.findById(r.getIdHuesped()));
        reserva.setCantHuesped(r.getCantHuesped());
        reserva.setFechaInicio(LocalDate.parse(r.getFechaInicio()));
        reserva.setCantNoches(r.getCantNoches());
        reserva.setDescuento(r.getDescuento());
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
        reserva.setEstado(r.getEstado());

        return reserva;
    }   

    public List<CeldaCalendarioDTO> obtenerMatrizDisponibilidad(String inicioStr, String finStr) {
    LocalDate fechaInicio = LocalDate.parse(inicioStr);
    LocalDate fechaFin = LocalDate.parse(finStr);

    // 1. Obtener TODAS las habitaciones (para saber cuáles pintar de verde/libre)
    List<Habitacion> todasLasHabitaciones = habitacionService.obtenerTodas();

    // 2. Obtener solo las reservas que ocupan lugar en esas fechas
    List<DetalleReserva> ocupaciones = detalleService.buscarReservasEnConflicto(fechaInicio, fechaFin);

    // 3. Crear la grilla de respuesta
    List<CeldaCalendarioDTO> grilla = new ArrayList<>();

    // Iteramos día por día dentro del rango solicitado
    for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
        
        final LocalDate fechaActual = fecha; // Variable final para usar en lambda

        for (Habitacion habitacion : todasLasHabitaciones) {
            
            String estado = "LIBRE"; // Por defecto está libre (Verde)

            // Verificamos si esta habitación está en la lista de ocupadas PARA ESTE DÍA ESPECÍFICO
            // Usamos 'stream' para filtrar la lista de 'ocupaciones' que trajimos de la BD
            boolean estaOcupada = ocupaciones.stream().anyMatch(detalle -> {
                // A. ¿Es la misma habitación?
                boolean mismaHabitacion = detalle.getHabitacion().getIdHabitacion().equals(habitacion.getIdHabitacion());
                
                // B. ¿La fecha actual cae dentro del rango de esa reserva?
                LocalDate inicioReserva = detalle.getReserva().getFechaInicio();
                LocalDate finReserva = inicioReserva.plusDays(detalle.getCantidadNoches());
                
                // La fecha actual debe ser >= inicio Y < fin (asumiendo que el día de salida queda libre)
                boolean fechaCaeEnReserva = !fechaActual.isBefore(inicioReserva) && fechaActual.isBefore(finReserva);

                return mismaHabitacion && fechaCaeEnReserva;
            });

            if (estaOcupada) {
                estado = "OCUPADA"; // Rojo
                // Opcional: Podrías chequear detalle.getReserva().getEstado() para diferenciar entre RESERVADA y OCUPADA
            }

            grilla.add(new CeldaCalendarioDTO(fechaActual.toString(), habitacion.getIdHabitacion(), estado));
        }
    }

    return grilla;
}

}
