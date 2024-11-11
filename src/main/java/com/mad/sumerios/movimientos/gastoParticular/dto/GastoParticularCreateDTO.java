package com.mad.sumerios.movimientos.gastoParticular.dto;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @NotBlank
    private String titulo;
    @NotNull
    private FormaPago formaPago;
    @NotNull
    private String comprobante;
    private String descripcion;
    @NotNull
    private Double totalFinal;
    @NotNull
    private boolean pagoConsorcio;
}
