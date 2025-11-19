package com.Diseno.TPDiseno2025.Domain;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class UsuarioId {
    private String nombre;
    private String psw;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioId that = (UsuarioId) o;
        return Objects.equals(nombre, that.nombre) &&
               Objects.equals(psw, that.psw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, psw);
    }
}
