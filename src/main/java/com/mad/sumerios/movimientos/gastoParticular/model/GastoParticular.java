package com.mad.sumerios.movimientos.gastoParticular.model;

import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
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
@Table(name = "tbl_gasto_particular")
public class GastoParticular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto_particular")
    private Long idGastoParticular;
    private final TipoEgreso TIPO_EGRESO = TipoEgreso.GASTOS_PARTICULARES;

    //  REFERENCIAS
    @NotNull
    private Long idConsorcio;
    @NotNull
    @Column(name = "id_proveedor")
    private Long idProveedor;
    @NotNull
    private Long idUf;

    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exp")
    @JsonBackReference
    private Expensa expensa;

    //  DATOS DE PAGO
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @NotBlank
    private String titulo;

    @NotNull
    private FormaPago formaPago;
    @NotNull
    private String comprobante;
    private String descripcion;

    //  VALOR
    @NotNull
    private Double totalFinal;
    @NotNull
    private boolean pagoConsorcio;


}
