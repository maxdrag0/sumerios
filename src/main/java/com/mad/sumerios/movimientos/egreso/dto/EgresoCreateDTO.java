package com.mad.sumerios.movimientos.egreso.dto;

import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
public class EgresoCreateDTO {
    @NotNull
    private Long idConsorcio;
    @NotNull
    private Long idProveedor;
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
    private TipoEgreso tipoEgreso;
    @NotNull
    private FormaPago formaPago;
    private String Comprobante;
    private String descripcion;

    //  VALOR
    @NotNull
    private CategoriaEgreso categoriaEgreso;
    @NotNull
    private Double totalFinal;

}
