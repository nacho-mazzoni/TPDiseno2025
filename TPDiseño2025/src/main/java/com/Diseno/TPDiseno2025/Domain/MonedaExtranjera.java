package com.Diseno.TPDiseno2025.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MonedaExtranjera{
    @Id
    @JoinColumn(name="id_mediopago", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private MedioPago idMedioPago;

    @Column(name = "tipo_moneda", nullable = false)
    private String tipoMoneda;

    @Column(name = "valor_cambio", nullable = false)
    private Double valorCambio;
}
