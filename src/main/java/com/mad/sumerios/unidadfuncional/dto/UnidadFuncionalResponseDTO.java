package com.mad.sumerios.unidadfuncional.dto;

import lombok.Data;

@Data
public class UnidadFuncionalResponseDTO {
    private long idUf;
    private int unidadFuncional;
    private String titulo;
    private String apellidoPropietario;
    private String nombrePropietario;
    private Double deuda;
    private UfConsorcioDTO consorcioDTO;
}
