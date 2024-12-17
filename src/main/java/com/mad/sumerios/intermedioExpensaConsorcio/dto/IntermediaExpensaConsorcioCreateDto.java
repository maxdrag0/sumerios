package com.mad.sumerios.intermedioExpensaConsorcio.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IntermediaExpensaConsorcioCreateDto {
    @NotNull
    private Long idConsorcio;
    @NotNull
    private Long idExpensa;
}
