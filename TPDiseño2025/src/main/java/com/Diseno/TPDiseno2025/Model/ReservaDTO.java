package com.Diseno.TPDiseno2025.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTO {
    
    @NotNull
    private Integer idReserva;

    @NotBlank
    @Size(max = 10)
    private String fechaInicio;

    @NotBlank
    private Integer cantNoches;

    @NotNull
    private Integer idCliente;

    @NotNull
    private Integer idHabitacion;

    @NotNull
    private Integer cantHuesped;

    @NotNull
    private Boolean descuento;

}
