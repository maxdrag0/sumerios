package com.mad.sumerios.consorcio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsorcioCreateDTO {
    @NotBlank
    private String nombre;
    @NotBlank
    private String direccion;
    @NotBlank
    private String ciudad;
    @NotNull
    private Long idAdm;

    private String cuit;
    private String titulo;
    private String cbu;
    private String banco;
    private String numCuenta;
    private String alias;
}
