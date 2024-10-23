package com.mad.sumerios.administracion.dto;

import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class AdministracionConsorcioDTO {
    private long idConsorcio;
    private String nombre;
    private String direccion;
}
