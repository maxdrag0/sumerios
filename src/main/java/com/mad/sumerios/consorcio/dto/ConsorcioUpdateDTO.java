package com.mad.sumerios.consorcio.dto;

import lombok.Data;

@Data
public class ConsorcioUpdateDTO {

    private long idConsorcio;

    private String nombre;
    private String direccion;
    private String ciudad;
    private Long idAdm;
    private String cuit;
    private String titulo;
    private String cbu;
    private String banco;
    private String numCuenta;
    private String alias;
}
