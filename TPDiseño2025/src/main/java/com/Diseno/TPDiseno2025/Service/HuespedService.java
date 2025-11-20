package com.Diseno.TPDiseno2025.Service;

import com.Diseno.TPDiseno2025.Domain.Huesped;

import java.util.Optional;
import java.util.List;

public interface HuespedService {
 
    void crearHuesped(Huesped h);
    void modificarHuesped(String tipoDni, String numOriginal, Huesped hActualizado);
    void eliminarHuesped(Huesped h);
    Huesped obtenerHuesped(String tipoDni, String num);
    List<Huesped> obtenerTodos();
    
    
}

