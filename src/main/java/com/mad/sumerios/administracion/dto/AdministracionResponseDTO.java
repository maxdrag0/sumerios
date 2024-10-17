package com.mad.sumerios.administracion.dto;

import com.mad.sumerios.appuseradmin.dto.AppUserAdminRegisterDTO;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class AdministracionResponseDTO {
    private long idAdm;
    private String nombre;
    private String cuit;
    private String mail;
    private String telefono;
    private String direccion;
    private AppUserAdminRegisterDTO administrador;  // Información del administrador
    private List<ConsorcioResponseDTO> consorcios;  // Lista de consorcios asociados
}

