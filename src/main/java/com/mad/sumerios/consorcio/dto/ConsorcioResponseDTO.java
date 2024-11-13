package com.mad.sumerios.consorcio.dto;

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
    private String cuit;
    private String titulo;
    private String cbu;
    private String numCuenta;
    private String alias;
    private ConsorcioAdmDTO administracion;
    private List<ConsorcioUfDTO> unidades;
    private EstadoCuentaConsorcioDTO estadoCuentaConsorcioDTO;
}
