package com.mad.sumerios.intermedioExpensaConsorcio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "tbl_intermedia_expensas_consorcio")
public class IntermediaExpensaConsorcio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exp")
    private long idIntermedia;

    @NotNull
    private Long idConsorcio;

    @NotNull
    private Long idExpensa;
}
