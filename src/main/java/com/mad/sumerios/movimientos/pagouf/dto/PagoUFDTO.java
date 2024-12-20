package com.mad.sumerios.movimientos.pagouf.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Data
public class PagoUFDTO {
    private Long idPagoUF;
    private Long idExpensa;
    private YearMonth periodo;
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    private double valor;
    private FormaPago formaPago;
    private String descripcion;
}
