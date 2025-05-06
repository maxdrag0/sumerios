package com.mad.sumerios.estadocuentauf.dto;

import lombok.Data;

import java.time.YearMonth;

@Data
public class EstadoCuentaUfDTO {
    private Long idEstadoCuentaUf;
    private Long idUf;
    private YearMonth periodo;
    private Double totalMesPrevio;
    private Double deuda;
    private Double intereses;
    private Double totalA;
    private Double totalB;
    private Double totalC;
    private Double totalD;
    private Double totalE;
    private Double gastoParticular;
    private Double redondeo;
    private Double totalExpensa;
    private Boolean segundoVencimientoActivo;
    private Double segundoVencimiento;
    private Double saldoFinal;
    private Double saldoExpensa;
    private Double saldoIntereses;

}
