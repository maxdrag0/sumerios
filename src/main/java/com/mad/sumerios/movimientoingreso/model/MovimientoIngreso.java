package com.mad.sumerios.movimientoingreso.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_movimiento_ingreso")
public class MovimientoIngreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_ingreso")
    private Long idMovimientoIngreso;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @JsonBackReference
    private Consorcio consorcio;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @NotNull
    private double valor;

}
