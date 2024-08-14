package com.mad.sumerios.movimientoegreso.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.enums.TipoMovimiento;
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
public class MovimientoEgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_egreso")
    private Long idMovimientoEgreso;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @JsonBackReference
    private Consorcio consorcio;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @NotBlank
    private String titulo;
    private String descripcion;
    @NotNull
    private double valor;
    private TipoMovimiento tipoMovimiento;



}
