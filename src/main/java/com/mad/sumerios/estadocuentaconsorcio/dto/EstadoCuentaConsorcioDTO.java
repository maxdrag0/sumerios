package com.mad.sumerios.estadocuentaconsorcio.dto;

import lombok.Data;

@Data
public class EstadoCuentaConsorcioDTO {
    private Long idEstadoCuentaConsorcio;
    private Double total;
    private Double efectivo;
    private Double banco;
    private Double fondoAdm;
    private Double totalAlCierre;

}
