package com.mad.sumerios.consorcio.dto;

import com.mad.sumerios.enums.TipoCuenta;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ConsorcioResponseDTO {
    private long idConsorcio;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String banco;
    private String cuit;
    private String titularCuenta;
    private TipoCuenta tipoCuenta;
    private String cbu;
    private String numCuenta;
    private String alias;
    private Double porcentajeIntereses;
    private Boolean segundoVencimiento;
    private Double porcentajeSegundoVencimiento;
    private Long idAdm;
    private EstadoCuentaConsorcioDTO estadoCuentaConsorcioDTO;

    private String codigoAcceso;
}
