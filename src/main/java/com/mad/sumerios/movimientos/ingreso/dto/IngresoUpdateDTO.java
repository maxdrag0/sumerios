package com.mad.sumerios.movimientos.ingreso.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class IngresoUpdateDTO {
    @NotNull
    private Long idIngreso;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private Long idProveedor;
    private double valor;
    private String titulo;
    private String descripcion;
    private FormaPago formaPago;
}
