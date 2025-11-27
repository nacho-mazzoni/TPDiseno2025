package com.Diseno.TPDiseno2025.Repository;

import com.Diseno.TPDiseno2025.Domain.Estadia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadiaRepository extends JpaRepository<Estadia, Integer> {
}