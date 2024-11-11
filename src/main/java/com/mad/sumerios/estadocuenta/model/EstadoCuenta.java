package com.mad.sumerios.estadocuenta.model;

import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_estado_cuenta")
public class EstadoCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cuenta")
    private long idEstadoCuenta;

    @OneToOne(mappedBy = "estadoCuenta")
    private Consorcio consorcio;

    @NotNull
    private Double efectivo;
    @NotNull
    private Double banco;
    @NotNull
    private Double fondoAdm;
    @NotNull
    private Double total;
}
