package com.mad.sumerios.movimientos.egreso.dto;

import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.enums.FormaPago;
import lombok.Data;

import java.util.Date;

@Data
public class EgresoResponseDTO {
    private Long idEgreso;
    private Long idConsorcio;
    private Long idProveedor;
    private Long idExpensa;
    private Date fecha;
    private String titulo;
    private FormaPago formaPago;
    private String comprobante;
    private String descripcion;
    private Double totalFinal;
    private CategoriaEgreso categoriaEgreso;
}
