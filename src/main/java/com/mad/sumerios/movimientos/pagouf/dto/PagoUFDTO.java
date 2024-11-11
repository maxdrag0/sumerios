package com.mad.sumerios.movimientos.pagouf.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class PagoUFDTO {
    private Long idPagoUF;
    private Long IdExpensa;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private double valor;
    private FormaPago formaPago;
    private String descripcion;
}
