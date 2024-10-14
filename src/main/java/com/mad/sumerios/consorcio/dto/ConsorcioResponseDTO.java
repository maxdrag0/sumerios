package com.mad.sumerios.consorcio.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConsorcioResponseDTO {
    private long idConsorcio;
    private String nombre;
    private String direccion;
}
