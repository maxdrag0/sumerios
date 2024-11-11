package com.mad.sumerios.movimientos.pagouf.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class PagoUFCreateDTO {
    @NotNull
    private Long idUf;
    @NotNull
    private Long idConsorcio;
    @NotNull
    private Long idExpensa;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @NotNull
    private double valor;
    @NotNull
    private FormaPago formaPago;
    private String descripcion;
}
