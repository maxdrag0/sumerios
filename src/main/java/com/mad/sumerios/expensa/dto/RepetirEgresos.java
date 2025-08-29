package com.mad.sumerios.expensa.dto;

import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class RepetirEgresos {
    private Boolean repetirEgresos;
    private List<EgresoResponseDTO> egresos;
}
