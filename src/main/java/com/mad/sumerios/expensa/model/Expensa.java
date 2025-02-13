package com.mad.sumerios.expensa.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_expensas")
public class Expensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exp")
    private long idExpensa;

    @NotNull
    private Long idConsorcio;

    @NotNull
    private YearMonth periodo;

    @NotNull
    private Double porcentajeIntereses;

    private Double porcentajeSegundoVencimiento;

//  MOVIMIENTOS
    @OneToMany(mappedBy = "expensa",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Egreso> egresos;


    @OneToMany(mappedBy = "expensa",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<GastoParticular> gastosParticulares;

    @OneToMany(mappedBy = "expensa",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ingreso> ingresos;

    @OneToMany(mappedBy = "expensa",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<PagoUF> pagoUFS;



}
