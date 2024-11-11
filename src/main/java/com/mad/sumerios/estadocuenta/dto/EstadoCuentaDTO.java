package com.mad.sumerios.estadocuenta.dto;

import lombok.Data;

@Data
public class EstadoCuentaDTO {
    private Long idEstadoCuenta;
    private Double total;
    private Double efectivo;
    private Double banco;
    private Double fondoAdm;
}
