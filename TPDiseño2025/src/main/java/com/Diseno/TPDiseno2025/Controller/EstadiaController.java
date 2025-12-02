package com.Diseno.TPDiseno2025.Controller;

import com.Diseno.TPDiseno2025.Model.OcuparHabitacionRequest;
import com.Diseno.TPDiseno2025.Service.EstadiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadia")
public class EstadiaController {

    @Autowired
    private EstadiaService gestorEstadia;

    @PostMapping("/ocupar")
    public ResponseEntity<String> ocuparHabitacion(@RequestBody OcuparHabitacionRequest request) {
        try {
            // Llamada al GestorEstadia.OcuparHabitacion(ListaHabitaciones...)
            gestorEstadia.ocuparHabitacion(request);
            return ResponseEntity.ok("Habitaciones ocupadas y estadías registradas con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al ocupar habitación: " + e.getMessage());
        }
    }
    
}