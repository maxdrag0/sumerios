package com.mad.sumerios.copiaestadocuentaconsorcio.model;

import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_copia_estado_cuenta_consorcio")
public class CopiaEstadoCuentaConsorcio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_copia_estado_cuenta_consorcio")
    private long idCopiaEstadoCuentaConsorcio;

    @NotNull
    private Long idConsorcio;
    @NotNull
    private YearMonth periodo;
    @NotNull
    private Double efectivo;
    @NotNull
    private Double banco;
    @NotNull
    private Double fondoAdm;
    @NotNull
    private Double total;
}
