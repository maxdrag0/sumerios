package com.mad.sumerios.estadocuentauf.dto;

import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import lombok.Data;

import java.time.YearMonth;
import java.util.List;

@Data
public class EstadoCuentaUfUpdatePeriodo {
    private List<UnidadFuncionalResponseDTO> unidadFuncionalList;
    private YearMonth periodo;
}
