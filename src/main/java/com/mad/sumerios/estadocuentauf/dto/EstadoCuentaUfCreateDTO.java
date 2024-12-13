package com.mad.sumerios.estadocuentauf.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.YearMonth;

@Data
public class EstadoCuentaUfCreateDTO {

    @NotNull
    private Long idUf;
    @NotNull
    private YearMonth periodo;

    public EstadoCuentaUfCreateDTO (Long idUf){
        this.idUf = idUf;
    }
}
