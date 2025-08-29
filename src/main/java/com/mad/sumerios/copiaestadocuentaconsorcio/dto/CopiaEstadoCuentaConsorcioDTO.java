package com.mad.sumerios.copiaestadocuentaconsorcio.dto;

import lombok.Data;

import java.time.YearMonth;

@Data
public class CopiaEstadoCuentaConsorcioDTO {
    private long idCopiaEstadoCuentaConsorcio;
    private Long idEstadoCuentaConsorcio;
    private Long idConsorcio;
    private YearMonth periodo;
    private Double efectivo;
    private Double banco;
    private Double fondoAdm;
    private Double total;
    private Double totalAlCierre;
}
