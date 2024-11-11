package com.mad.sumerios.movimientos.ingreso.model;

import com.mad.sumerios.enums.FormaPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.mad.sumerios.expensa.model.Expensa;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @NotNull
    private Long idConsorcio;

    @NotNull
    private Long idProveedor;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exp")
    @JsonBackReference
    private Expensa expensa;

//  DATO DEL INGRESO
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @NotBlank
    private String titulo;
    @NotNull
    private FormaPago formaPago;
    @NotBlank
    private String descripcion;
    @NotNull
    private double valor;
}
