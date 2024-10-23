package com.mad.sumerios.unidadfuncional.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UnidadFuncionalCreateDTO {
    @NotNull
    private Long idConsorcio;
    @NotNull
    private int unidadFuncional;
    @NotBlank
    @Size(max = 5)
    private String titulo;

    @NotNull
    @Min(0)
    private Double porcentajeUnidad;

    // DATOS PERSONAS
    // PROPIETARIO
    @NotNull
    private String apellidoPropietario;
    @NotNull
    private String nombrePropietario;
    private String mailPropietario;
    private String telefonoPropietario;
    // INQUILINO
    private String apellidoInquilino;
    private String nombreInquilino;
    private String mailInquilino;
    private String telefonoInquilino;

    // ESTADO DE CUENTA
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
