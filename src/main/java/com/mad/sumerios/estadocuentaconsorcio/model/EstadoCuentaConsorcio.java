package com.mad.sumerios.estadocuentaconsorcio.model;

import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_estado_cuenta_consorcio")
public class EstadoCuentaConsorcio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cuenta_consorcio")
    private long idEstadoCuentaConsorcio;

    @OneToOne(mappedBy = "estadoCuentaConsorcio")
    private Consorcio consorcio;

    @NotNull
    private Double efectivo;
    @NotNull
    private Double banco;
    @NotNull
    private Double fondoAdm;
    @NotNull
    private Double total;
    private Double totalAlCierre;
}
