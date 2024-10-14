package com.mad.sumerios.pendientes.movimientos.pagouf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_pago_uf")
public class PagoUF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago_uf")
    private Long idPagoUF;

//    @NotNull
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_exp")
//    @JsonBackReference
//    private Expensa expensa;

//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_consorcio")
//    @NotNull
//    @JsonBackReference
//    private Consorcio consorcio;

//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_uf")
//    @JsonBackReference
//    private UnidadFuncional unidadFuncional;

//  DATO DEL INGRESO
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotNull
    private double valor;

    @NotNull
    private FormaPago formaPago;

    private String descripcion;
}
