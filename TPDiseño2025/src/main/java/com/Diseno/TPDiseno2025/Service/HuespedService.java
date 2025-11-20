package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Huesped;

public interface HuespedService {
 
    void crearHuesped(Huesped h);
    void modificarHuesped(String tipoDni, Integer numOriginal, Huesped hActualizado);
    void eliminarHuesped(Huesped h);
    Huesped obtenerHuesped(String tipoDni, Integer num);
    List<Huesped> obtenerTodos();
    
    
}

