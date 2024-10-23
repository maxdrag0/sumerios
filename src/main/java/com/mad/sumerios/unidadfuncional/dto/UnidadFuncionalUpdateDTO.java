package com.mad.sumerios.unidadfuncional.dto;

import lombok.Data;

@Data
public class UnidadFuncionalUpdateDTO {
    private Long idUf;
    private Long idConsorcio;
    private int unidadFuncional;
    private String titulo;
    private Double porcentajeUnidad;

    // DATOS PERSONAS
    // PROPIETARIO
    private String apellidoPropietario;
    private String nombrePropietario;
    private String mailPropietario;
    private String telefonoPropietario;
    // INQUILINO
    private String apellidoInquilino;
    private String nombreInquilino;
    private String mailInquilino;
    private String telefonoInquilino;

    // EST ADO DE CUENTA
    private Double deuda;
    private Double intereses;
    private Double totalA;
    private Double totalB;
    private Double totalC;
    private Double totalD;
    private Double totalE;
    private Double gastoParticular;
    private Double totalFinal;
}
