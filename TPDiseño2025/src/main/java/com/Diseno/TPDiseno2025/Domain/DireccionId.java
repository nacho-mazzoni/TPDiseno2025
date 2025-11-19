package com.Diseno.TPDiseno2025.Domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Embeddable
@Getter
@Setter
public class DireccionId implements Serializable {
    private String calle;
    private String numero;
    private String departamento;
    private String piso;
    private String cod_postal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DireccionId that = (DireccionId) o;
        return Objects.equals(calle, that.calle) &&
               Objects.equals(numero, that.numero) &&
               Objects.equals(departamento, that.departamento) &&
               Objects.equals(piso, that.piso) &&
               Objects.equals(cod_postal, that.cod_postal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, numero, departamento, piso, cod_postal);
    }
}
