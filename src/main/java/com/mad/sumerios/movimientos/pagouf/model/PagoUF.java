package com.mad.sumerios.movimientos.pagouf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.mad.sumerios.expensa.model.Expensa;
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

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uf")
    @JsonBackReference
    private UnidadFuncional unidadFuncional;

    @NotNull
    private Long idConsorcio;

//  DATO DEL INGRESO
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotNull
    private double valor;

    @NotNull
    private FormaPago formaPago;

    private String descripcion;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exp")
    @JsonBackReference
    private Expensa expensa;

}
