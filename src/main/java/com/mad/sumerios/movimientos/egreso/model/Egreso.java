package com.mad.sumerios.movimientos.egreso.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.expensa.model.Expensa;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_egreso")
public class Egreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_egreso")
    private Long idEgreso;

    //  REFERENCIAS
    @NotNull
    private Long idConsorcio;
    @NotNull
    @Column(name = "id_proveedor")
    private Long idProveedor;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exp")
    @JsonBackReference
    private Expensa expensa;
    @NotNull
    private YearMonth periodo;

    //  DATOS DEL GASTO
    @NotNull
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;
    @NotBlank
    private String titulo;
    @NotNull
    private TipoEgreso tipoEgreso;
    @NotNull
    private FormaPago formaPago;
    @NotNull
    private String comprobante;
    private String descripcion;

    //  VALOR
    @NotNull
    private Double totalFinal;
    @NotNull
    private CategoriaEgreso categoriaEgreso;

}
