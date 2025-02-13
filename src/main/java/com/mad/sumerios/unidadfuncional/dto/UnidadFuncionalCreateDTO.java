package com.mad.sumerios.unidadfuncional.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.YearMonth;

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
    @NotNull
    @Min(0)
    private Double porcentajeUnidadB;
    @NotNull
    @Min(0)
    private Double porcentajeUnidadC;
    @NotNull
    @Min(0)
    private Double porcentajeUnidadD;
    @NotNull
    @Min(0)
    private Double porcentajeUnidadE;
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

    private YearMonth periodo;
}
