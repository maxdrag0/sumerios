package com.mad.sumerios.movimientos.egreso.dto;

import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class EgresoUpdateDTO {
    private Long idEgreso;
    private Long idConsorcio;
    private Long idProveedor;
    private Date fecha;
    private TipoEgreso tipoEgreso;
    private String titulo;
    private FormaPago formaPago;
    private String comprobante;
    private String descripcion;
    private Double totalFinal;
    private Double totalA;
    private Double totalB;
    private Double totalC;
    private Double totalD;
    private Double totalE;
}
