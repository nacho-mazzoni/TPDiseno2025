package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.TipoHabitacion;

public class TipoHabitacionServiceImp implements TipoHabitacionService{

    @Override
    public Double getPrecioByTipo(Integer idTipo){
        switch(idTipo){
            case 1:
                return 50800.00;
                
            case 2:
                return 70230.00;
                
            case 3:
                return 90560.00;
                
            case 4:
                return 110500.00;
            case 5:
                return 128600.00;
            default:
                return 0.00;
        }
    }

    @Override
    public TipoHabitacion getTipoByIdTipo(Integer idTipo){
        switch(idTipo){
            case 1:
                return new TipoHabitacion(1, "Individual Estándar");
                
            case 2:
                return new TipoHabitacion(2, "Doble Estándar ");
                
            case 3:
                return new TipoHabitacion(3, "Doble Superior");
                
            case 4:
                return new TipoHabitacion(4, "Superior Family Plan");
            case 5:
                return new TipoHabitacion(5, "Suite Doble");
            default:
                return new TipoHabitacion();
        }
    }
    
 
 
 
}
