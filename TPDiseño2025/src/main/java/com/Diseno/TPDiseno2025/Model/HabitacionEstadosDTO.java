package com.Diseno.TPDiseno2025.Model;

import java.util.ArrayList;
import java.util.List;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitacionEstadosDTO {
    
    @NotBlank
    private Integer idHabitacion;

    @NotNull
    private TipoHabitacion idTipo;
    
    private List<String> estados = new ArrayList<>();
}
