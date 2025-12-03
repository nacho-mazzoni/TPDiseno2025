package com.Diseno.TPDiseno2025.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

import com.Diseno.TPDiseno2025.Domain.Estadia;
import com.Diseno.TPDiseno2025.Domain.Reserva;

public interface EstadiaRepository extends JpaRepository<Estadia, Integer>{
	boolean existsByReserva(Reserva reserva);

=======
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Estadia;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer>{
    
>>>>>>> e2b8bfeacfd638d0f5c6ad18c7233cd849ed9c33
}
