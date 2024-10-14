package com.mad.sumerios.pendientes.movimientos.egreso.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import com.mad.sumerios.pendientes.expensa.model.Expensa;
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
    @JoinColumn(name = "id_exp")
    @JsonBackReference
    private Expensa expensa;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @NotNull
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

    @NotNull
    @Column(name = "id_proveedor")
    private Long idProveedor;

    @NotNull
    private FormaPago formaPago;
    private String descripcion;

//  EXPENSA
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
    private boolean esParticular;
    private Double gastoParticular;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uf",
                nullable = true)
    private UnidadFuncional unidadFuncional;
}
