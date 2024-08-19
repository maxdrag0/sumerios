package com.mad.sumerios.ingreso.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.enums.FormaPago;
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
@Table(name = "tbl_movimiento_ingreso")
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_ingreso")
    private Long idIngreso;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @JsonBackReference
    private Consorcio consorcio;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uf")
    @JsonBackReference
    private UnidadFuncional unidadFuncional;

//  DATO DEL INGRESO
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotNull
    private double valor;

    @NotBlank
    private String descripcion;

    @NotNull
    private FormaPago formaPago;

}
