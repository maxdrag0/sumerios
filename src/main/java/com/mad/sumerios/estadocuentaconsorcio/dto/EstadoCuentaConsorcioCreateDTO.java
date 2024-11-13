package com.mad.sumerios.estadocuentaconsorcio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCuentaConsorcioCreateDTO {

    @NotNull
    private Long idConsorcio;
    @NotNull
    private Double efectivo;
    @NotNull
    private Double banco;
    @NotNull
    private Double fondoAdm;
    @NotNull
    private Double total;
}
