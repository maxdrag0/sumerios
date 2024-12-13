package com.mad.sumerios.expensa.dto;

import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.YearMonth;

@Data
public class ExpensaCreateDTO {

    @NotNull
    private Long idConsorcio;

    @NotNull
    private YearMonth periodo;

    @NotNull
    private Double porcentajeIntereses;

    private Double porcentajeSegundoVencimiento;
}
