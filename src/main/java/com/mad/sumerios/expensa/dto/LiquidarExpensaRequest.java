package com.mad.sumerios.expensa.dto;

import lombok.Data;

@Data
public class LiquidarExpensaRequest {
    private Long idExpensa;
    private RepetirEgresos repetirEgresos;
    private Boolean segundoVencimiento;
    private Boolean mostrarFondoAdm;
    private ExpensaCreateDTO expensaCreateDTO;
}
