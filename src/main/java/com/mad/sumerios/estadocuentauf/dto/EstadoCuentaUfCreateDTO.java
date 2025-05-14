package com.mad.sumerios.estadocuentauf.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;

@Data
public class EstadoCuentaUfCreateDTO {

    @NotNull
    private Long idUf;
    private YearMonth periodo;


    public EstadoCuentaUfCreateDTO (Long idUf, YearMonth periodo){
        this.idUf = idUf;
        this.periodo = periodo;
    }
}
