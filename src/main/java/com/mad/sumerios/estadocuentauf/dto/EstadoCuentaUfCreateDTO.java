package com.mad.sumerios.estadocuentauf.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.YearMonth;

@Data
public class EstadoCuentaUfCreateDTO {

    @NotNull
    private Long idUf;

    public EstadoCuentaUfCreateDTO (Long idUf){
        this.idUf = idUf;
    }
}
