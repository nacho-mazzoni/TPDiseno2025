package com.Diseno.TPDiseno2025.Model;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoHabitacionDTO {
    
    @NotNull
    private Integer idTipo;

    @NotBlank
    private String nombreTipo;

    @NotNull
    private Double precioNoche;

    @NotNull
    private Integer cantidaDisponible;
}
