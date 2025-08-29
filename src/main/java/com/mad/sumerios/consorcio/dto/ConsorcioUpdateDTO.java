package com.mad.sumerios.consorcio.dto;

import com.mad.sumerios.enums.TipoCuenta;
import lombok.Data;

@Data
public class ConsorcioUpdateDTO {
    private Long idAdm;
    private long idConsorcio;

    private String nombre;
    private String direccion;
    private String ciudad;
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
