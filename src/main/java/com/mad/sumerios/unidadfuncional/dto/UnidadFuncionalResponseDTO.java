package com.mad.sumerios.unidadfuncional.dto;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UnidadFuncionalResponseDTO {
    private long idUf;
    private Long idConsorcio;
    private int unidadFuncional;
    private String titulo;
    private Double porcentajeUnidad;
    private Double porcentajeUnidadB;
    private Double porcentajeUnidadC;
    private Double porcentajeUnidadD;
    private Double porcentajeUnidadE;
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
