package com.mad.sumerios.movimientos.ingreso.dto;

import com.mad.sumerios.enums.FormaPago;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Data
public class IngresoResponseDTO extends IngresoUpdateDTO{

    private Long idIngreso;
    private Long idProveedor;
    private Long idExpensa;
    private YearMonth periodo;
    private LocalDate fecha;
    private double valor;
    private String titulo;
    private String descripcion;
    private FormaPago formaPago;
}
