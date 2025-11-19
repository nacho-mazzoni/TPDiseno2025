package com.Diseno.TPDiseno2025.Domain;

import java.time.LocalDate; 

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Huesped {
    
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String tipodni;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false)
    private String ocupacion;

    @Column(nullable = true)
    private String mail;

    @Column(nullable = true)
    private String pos_iva;

    @JoinColumns({
        @JoinColumn(name = "calle", referencedColumnName = "calle"),
        @JoinColumn(name = "numero", referencedColumnName = "numero"),
        @JoinColumn(name = "departamento", referencedColumnName = "departamento"),
        @JoinColumn(name = "piso", referencedColumnName = "piso"),
        @JoinColumn(name = "cod_postal", referencedColumnName = "cod_postal")
    })
    private Direccion direccion;

}
