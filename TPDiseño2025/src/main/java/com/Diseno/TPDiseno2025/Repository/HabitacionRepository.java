package com.Diseno.TPDiseno2025.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Diseno.TPDiseno2025.Domain.Habitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Integer>{
    @Override
    Optional<Habitacion> findById(Integer idHabitacion);

}
