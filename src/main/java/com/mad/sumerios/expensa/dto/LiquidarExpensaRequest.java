package com.mad.sumerios.expensa.dto;

import lombok.Data;

@Data
public class LiquidarExpensaRequest {
    private Long idExpensa;
    private Boolean repetirEgresos;
    private Boolean segundoVencimiento;
    private ExpensaCreateDTO expensaCreateDTO;
}
