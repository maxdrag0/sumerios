package com.mad.sumerios.movimientos.egreso.dto;

import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class EgresoCreateDTO {
    @NotNull
    private Long idConsorcio;
    @NotNull
    private Long idProveedor;
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @NotBlank
    private String titulo;
    @NotNull
    private TipoEgreso tipoEgreso;
    @NotNull
    private FormaPago formaPago;
    @NotNull
    private String Comprobante;
    private String descripcion;

    //  VALOR
    @NotNull
    private Double totalFinal;
    @NotNull
    private Double totalA;
    @NotNull
    private Double totalB;
    @NotNull
    private Double totalC;
    @NotNull
    private Double totalD;
    @NotNull
    private Double totalE;
}
