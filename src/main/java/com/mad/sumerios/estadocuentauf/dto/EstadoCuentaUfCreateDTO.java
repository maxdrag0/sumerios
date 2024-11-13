package com.mad.sumerios.estadocuentauf.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCuentaUfCreateDTO {

    @NotNull
    private Long idUf;
    @NotNull
    private Double deuda;
    @NotNull
    private Double intereses;
    @NotNull
    private Double totalA;
    @NotNull
    private Double totalB;
    @NotNull
    private Double totalC;
    @NotNull
    private Double totalD;
    @NotNull
    private Double totalE;
    @NotNull
    private Double gastoParticular;
    @NotNull
    private Double totalFinal;
    @NotNull
    private Double saldoExpensa;
    @NotNull
    private Double saldoIntereses;
}
