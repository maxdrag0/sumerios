package com.mad.sumerios.movimientos.egreso.dto;

import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class EgresoUpdateDTO {
    private Long idEgreso;
    private Long idConsorcio;
    private Long idProveedor;
    private Long idExpensa;
    private YearMonth periodo;
    private LocalDate fecha;
    private TipoEgreso tipoEgreso;
    private String titulo;
    private FormaPago formaPago;
    private String comprobante;
    private String descripcion;
    private Double totalFinal;
    private CategoriaEgreso categoriaEgreso;
}
