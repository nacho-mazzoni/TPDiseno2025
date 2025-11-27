package com.Diseno.TPDiseno2025.Model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OcuparHabitacionRequest {
    //Se lo nombra como Request ya que es un objeto que ingresa
    private List<Integer> idsHabitaciones; 
    private List<Integer> idsHuespedes;    
    private Double precio;                 
}