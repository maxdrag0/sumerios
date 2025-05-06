package com.mad.sumerios.estadocuentauf.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_estado_cuenta_uf")
public class EstadoCuentaUf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cuenta_uf")
    private long idEstadoCuentaUf;

    @OneToOne(mappedBy = "estadoCuentaUf", fetch = FetchType.LAZY)
    @JsonIgnore
    private UnidadFuncional unidadFuncional;

    private YearMonth periodo = YearMonth.now();
    private Double totalMesPrevio;

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
    private Double redondeo;

    // VALOR EXPENSA + INTERESES + DEUDA + REDONDEO
    @NotNull
    private Double totalExpensa;

    @NotNull
    private Boolean segundoVencimientoActivo;
    @NotNull
    private Double segundoVencimiento;
    // VALOR EXPENSA + INTERESES + DEUDA + REDONDEO - EL CUAL SE VA MODIFICANDO SEGUN PAGOS
    @NotNull
    private Double saldoFinal;

    // VALOR DE EXPENSAS + DEUDA EXPENSA O - SALDO A FAVOR
    @NotNull
    private Double saldoExpensa;
    //
    @NotNull
    private Double saldoIntereses;



    public void setSaldoInteresesCero(){
        this.saldoIntereses = (double) 0;
    }
}
