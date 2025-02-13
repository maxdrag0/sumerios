package com.mad.sumerios.movimientos.egreso.dto;

import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Data
public class EgresoResponseDTO {
    private Long idEgreso;
    private Long idConsorcio;
    private Long idProveedor;
    private Long idExpensa;
    private YearMonth periodo;
    private LocalDate fecha;
    private String titulo;
    private TipoEgreso tipoEgreso;
    private FormaPago formaPago;
    private String comprobante;
    private String descripcion;
    private CategoriaEgreso categoriaEgreso;
    private Double totalFinal;

}
