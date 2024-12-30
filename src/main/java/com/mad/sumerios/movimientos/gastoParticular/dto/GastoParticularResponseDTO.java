package com.mad.sumerios.movimientos.gastoParticular.dto;

import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Data
public class GastoParticularResponseDTO {
    private Long idGastoParticular;
    private Long idProveedor;
    private Long idUf;
    private Long idConsorcio;
    private Long idExpensa;
    private YearMonth periodo;
    private LocalDate fecha;
    private String titulo;
    private FormaPago formaPago;
    private TipoEgreso tipoEgreso;
    private String comprobante;
    private String descripcion;
    private Boolean pagoConsorcio;
    private Double totalFinal;
}
