package com.mad.sumerios.intermedioExpensaConsorcio.dto;

import lombok.Data;

import java.time.YearMonth;

@Data
public class IntermediaExpensaConsorcioDto {
    private long idIntermedia;

    private Long idConsorcio;

    private Long idExpensa;

    private YearMonth periodo;
}
