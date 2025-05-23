package com.mad.sumerios.movimientos.pagouf.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class PagoUFCreateDTO {
    @NotNull
    private Long idUf;
    @NotNull
    private Long idConsorcio;
    @NotNull
    private Long idExpensa;
    @NotNull
    private YearMonth periodo;
    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @NotNull
    private double valor;
    @NotNull
    private FormaPago formaPago;
    private String descripcion;
}
