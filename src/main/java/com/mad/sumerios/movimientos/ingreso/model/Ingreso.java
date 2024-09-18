package com.mad.sumerios.movimientos.ingreso.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.expensa.model.Expensa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_movimiento_ingreso")
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingreso")
    private Long idIngreso;

//    @NotNull
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_exp")
//    @JsonBackReference
//    private Expensa expensa;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @NotNull
    @JsonBackReference
    private Consorcio consorcio;

//  DATO DEL INGRESO
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotNull
    private double valor;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descripcion;

    @NotNull
    private FormaPago formaPago;

}
