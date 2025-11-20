package com.Diseno.TPDiseno2025.Service;

import java.util.List;

import com.Diseno.TPDiseno2025.Domain.Juridica;
import com.Diseno.TPDiseno2025.Model.JuridicaDTO;

public interface JuridicaService {
    
    void crearJuridica(Integer cuit, String razonSocial, String telefono, String email);
    JuridicaDTO crearJuridicaDTO(Integer cuit, String razonSocial, String telefono, String email);
    Juridica get(Integer cuit);
    Integer getCuit(Juridica j);
    boolean existsByCuit(String cuit);
    List<Juridica> obtenerTodas();
    Juridica mapToEntJuridica(JuridicaDTO jdTO, Juridica j);
    JuridicaDTO mapToDTOJuridica(Juridica j, JuridicaDTO jdTO);
}
