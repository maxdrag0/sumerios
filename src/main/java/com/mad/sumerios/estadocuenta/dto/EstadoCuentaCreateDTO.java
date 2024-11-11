package com.mad.sumerios.estadocuenta.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCuentaCreateDTO {

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
