package com.mad.sumerios.consorcio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsorcioUpdateDTO {
    private Long idAdm;
    private long idConsorcio;

    private String nombre;
    private String direccion;
    private String ciudad;
    private String cuit;
    private String titulo;
    private String cbu;
    private String banco;
    private String numCuenta;
    private String alias;
}
