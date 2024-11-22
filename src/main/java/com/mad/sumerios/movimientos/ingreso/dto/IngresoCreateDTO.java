package com.mad.sumerios.movimientos.ingreso.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class IngresoCreateDTO {
    @NotNull
    private Long idConsorcio;
    @NotNull
    private Long idProveedor;
//    @NotNull
//    private Long idExpensa;
    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    @NotNull
    private double valor;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotNull
    private FormaPago formaPago;
}
