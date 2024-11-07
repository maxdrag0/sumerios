package com.mad.sumerios.movimientos.gastoParticular.dto;

import com.mad.sumerios.enums.FormaPago;
import lombok.Data;

import java.util.Date;

@Data
public class GastoParticularResponseDTO {
    private Long idGastoParticular;
    private Long idProveedor;
    private Long idUf;
    private Long idConsorcio;
    private Date fecha;
    private String titulo;
    private FormaPago formaPago;
    private String comprobante;
    private String descripcion;
    private Double totalFinal;
}
