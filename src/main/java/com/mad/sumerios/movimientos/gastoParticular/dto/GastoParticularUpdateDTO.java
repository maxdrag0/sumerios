package com.mad.sumerios.movimientos.gastoParticular.dto;


import com.mad.sumerios.enums.FormaPago;
import lombok.Data;

import java.util.Date;

@Data
public class GastoParticularUpdateDTO {
    private Long idGastoParticular;
    private Long idConsorcio;
    private Long idProveedor;
    private Long idUf;
    private Date fecha;
    private String titulo;
    private FormaPago formaPago;
    private String comprobante;
    private String descripcion;
    private Double totalFinal;
}
