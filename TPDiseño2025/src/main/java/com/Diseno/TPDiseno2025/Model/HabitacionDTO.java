package com.Diseno.TPDiseno2025.Model;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitacionDTO {
    
    @NotBlank
    private Integer idHabitacion;

    @NotNull
    @NotBlank
    private TipoHabitacion idTipo;

    @NotNull
    private Integer nochesDescuento;
    
    @NotNull
    @Size(max=50)
    private String estado;
}
