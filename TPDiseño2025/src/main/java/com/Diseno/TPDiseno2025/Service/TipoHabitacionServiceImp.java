package com.Diseno.TPDiseno2025.Service;

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
    
}
