package com.mad.sumerios.unidadfuncional.dto;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import lombok.Data;

@Data
public class UnidadFuncionalResponseDTO {
    private long idUf;
    private int unidadFuncional;
    private String titulo;
    private String apellidoPropietario;
    private String nombrePropietario;
    private Double porcentajeUnidad;
    private UfConsorcioDTO consorcio;
    private EstadoCuentaUfDTO estadoCuentaUfDTO;
}
