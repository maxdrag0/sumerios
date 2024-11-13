package com.mad.sumerios.estadocuentauf.model;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_estado_cuenta_uf")
public class EstadoCuentaUf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cuenta_uf")
    private long idEstadoCuentaUf;

    @OneToOne(mappedBy = "estadoCuentaUf")
    private UnidadFuncional unidadFuncional;

    @NotNull
    private Double deuda;
    @NotNull
    private Double intereses;
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
    @NotNull
    private Double gastoParticular;
    @NotNull
    private Double totalFinal;
    @NotNull
    private Double saldoExpensa;
    @NotNull
    private Double saldoIntereses;
}
