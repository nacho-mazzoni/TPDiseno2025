package com.Diseno.TPDiseno2025.Model;
import lombok.Data;

@Data
public class CeldaCalendarioDTO {
    private String fecha;       // "2025-03-01"
    private Integer idHabitacion;
    private String estado;      // "LIBRE", "OCUPADA", "RESERVADA"

    public CeldaCalendarioDTO(){}

    public CeldaCalendarioDTO(String fecha, Integer idHab, String estado){
        this.fecha = fecha;
        this.idHabitacion = idHab;
        this.estado = estado;
    }
}