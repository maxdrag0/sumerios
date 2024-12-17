package com.mad.sumerios.copiaestadocuentauf.dto;

import lombok.Data;

import java.time.YearMonth;

@Data
public class CopiaEstadoCuentaUfDTO {
    private long idCopiaEstadoCuentaUf;
    private long idEstadoCuentaUf;
    private long idUf;
    private YearMonth periodo;
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
