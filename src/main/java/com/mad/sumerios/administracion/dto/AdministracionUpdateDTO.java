package com.mad.sumerios.administracion.dto;

import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class AdministracionUpdateDTO {
    private long idAdm;
    private String nombre;
    private String cuit;
    private String mail;
    private String telefono;
    private String direccion;
    private Long administradorId;
    private List<ConsorcioResponseDTO> consorcios;
}
