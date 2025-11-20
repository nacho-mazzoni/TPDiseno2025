package com.Diseno.TPDiseno2025.Service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Diseno.TPDiseno2025.Domain.Huesped;
import com.Diseno.TPDiseno2025.Repository.HuespedRepository;

@Service
public class HuespedServiceImp implements HuespedService {

    @Autowired
    private HuespedRepository huespedRepository;

    @Override
    public void crearHuesped(Huesped h) {
        huespedRepository.save(h);
    }

    @Override
    public void modificarHuesped(String tipoDni, String numOriginal, Huesped hActualizado) {
        Huesped existente = obtenerHuesped(tipoDni, numOriginal);

        // acá actualizás los campos que quieras
        existente.setNombre(hActualizado.getNombre());
        existente.setApellido(hActualizado.getApellido());
        existente.setDireccion(hActualizado.getDireccion());
        // ... etc.

        huespedRepository.save(existente);
    }

    @Override
    public void eliminarHuesped(Huesped h) {
        huespedRepository.delete(h);
    }

    @Override
    public Huesped obtenerHuesped(String tipoDni, String num) {
        return huespedRepository.findByTipoDniAndNum(tipoDni, num)
                .orElse(null); // o .orElseThrow(...)
    }

    @Override
    public List<Huesped> obtenerTodos() {
        return huespedRepository.findAll();
    }
}
}
