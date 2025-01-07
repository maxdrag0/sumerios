package com.mad.sumerios.unidadfuncional.dto;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import lombok.Data;

@Data
public class UnidadFuncionalResponseDTO {
    private long idUf;
    private int unidadFuncional;
    private String titulo;
    private Double porcentajeUnidad;
    private UfConsorcioDTO consorcio;
    // PROPIETARIO
    private String apellidoPropietario;
    private String nombrePropietario;
    private String mailPropietario;
    private String telefonoPropietario;
    // INQUILINO
    private String apellidoInquilino;
    private String nombreInquilino;
    private String mailInquilino;
    private String telefonoInquilino;
    // ESTADO DE CUENTA
    private EstadoCuentaUfDTO estadoCuentaUfDTO;

}
