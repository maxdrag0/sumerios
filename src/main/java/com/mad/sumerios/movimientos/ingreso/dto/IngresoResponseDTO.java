package com.mad.sumerios.movimientos.ingreso.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class IngresoResponseDTO extends IngresoUpdateDTO{

    private Long idIngreso;
    private Long idProveedor;
    private Date fecha;
    private double valor;
    private String titulo;
    private String descripcion;
    private FormaPago formaPago;
}
