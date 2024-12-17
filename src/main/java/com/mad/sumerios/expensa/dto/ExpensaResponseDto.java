package com.mad.sumerios.expensa.dto;

import lombok.Data;

import java.time.YearMonth;

@Data
public class ExpensaResponseDto {
    private Long idExpensa;
    private Long idConsorcio;
    private YearMonth periodo;
    private Double porcentajeIntereses;
    private Double porcentajeSegundoVencimiento;
}
