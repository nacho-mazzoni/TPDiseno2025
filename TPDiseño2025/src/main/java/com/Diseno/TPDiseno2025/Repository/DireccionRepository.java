package com.Diseno.TPDiseno2025.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Diseno.TPDiseno2025.Domain.Direccion;
import com.Diseno.TPDiseno2025.Domain.DireccionId;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, DireccionId> {
    
    @Override
    Optional<Direccion> findById(DireccionId direccionId);

    Boolean existsByCalleAndNumeroAndLocalidaddAndProvinciaAndPais(
        String calle, 
        String numero, 
        String localidad, 
        String provincia, 
        String pais
    );

    List<Direccion> findByLocalidadStartingWithIgnoreCase(String localidad);

}
