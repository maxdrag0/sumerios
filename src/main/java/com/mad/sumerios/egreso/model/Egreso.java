package com.mad.sumerios.egreso.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.enums.TipoEgreso;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_movimiento_egreso")
public class Egreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_egreso")
    private Long idMovimientoEgreso;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @JsonBackReference
    private Consorcio consorcio;

//  DATOS DEL GASTO
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotBlank
    private String titulo;

    @NotNull
    private TipoEgreso tipoEgreso;

    private String descripcion;

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

//  GASTO PARTICULAR
    private Double gastoParticular;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uf")
    private UnidadFuncional unidadFuncional;
}
