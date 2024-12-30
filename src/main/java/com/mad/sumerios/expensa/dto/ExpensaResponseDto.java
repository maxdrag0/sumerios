package com.mad.sumerios.expensa.dto;

import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularResponseDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoResponseDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import lombok.Data;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExpensaResponseDto {
    private Long idExpensa;
    private Long idConsorcio;
    private YearMonth periodo;
    private Double porcentajeIntereses;
    private Double porcentajeSegundoVencimiento;
    private List<EgresoResponseDTO> egresos = new ArrayList<>();
    private List<IngresoResponseDTO> ingresos = new ArrayList<>();
    private List<GastoParticularResponseDTO> gp = new ArrayList<>();
    private List<PagoUFDTO> pagoUf = new ArrayList<>();
}
