package com.mad.sumerios.copiaestadocuentauf.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_copia_estado_cuenta_uf")
public class CopiaEstadoCuentaUf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_copia_estado_cuenta_uf")
    private long idCopiaEstadoCuentaUf;
    @NotNull
    private long idEstadoCuentaUf;
    @NotNull
    private long idUf;
    //
    @NotNull
    private YearMonth periodo;
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
    private Double totalExpensa;
    @NotNull
    private Double saldoFinal;
    @NotNull
    private Double saldoExpensa;
    @NotNull
    private Double saldoIntereses;

}
