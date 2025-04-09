package com.mad.sumerios.estadocuentaconsorcio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadoCuentaConsorcioCreateDTO {

    @NotNull
    private Long idConsorcio;


    public EstadoCuentaConsorcioCreateDTO (Long idConsorcio){
        this.idConsorcio = idConsorcio;
    }
}
