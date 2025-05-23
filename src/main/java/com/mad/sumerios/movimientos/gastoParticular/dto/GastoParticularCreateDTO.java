package com.mad.sumerios.movimientos.gastoParticular.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Data
public class GastoParticularCreateDTO {

    @NotNull
    private Long idConsorcio;
    @NotNull
    private Long idProveedor;
    @NotNull
    private Long idUf;
    @NotNull
    private Long idExpensa;
    @NotNull
    private YearMonth periodo;
    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @NotBlank
    private String titulo;
    @NotNull
    private FormaPago formaPago;
    private String comprobante;
    private String descripcion;
    @NotNull
    private Double totalFinal;
    @NotNull
    private Boolean pagoConsorcio;
}
