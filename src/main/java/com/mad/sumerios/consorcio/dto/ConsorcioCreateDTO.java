package com.mad.sumerios.consorcio.dto;

import com.mad.sumerios.enums.TipoCuenta;
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
    private String titularCuenta;
    private TipoCuenta tipoCuenta;
    private String cbu;
    private String banco;
    private String numCuenta;
    private String alias;

    private Double porcentajeIntereses;
    private Boolean segundoVencimiento;
    private Double porcentajeSegundoVencimiento;

    private String codigoAcceso;
}
