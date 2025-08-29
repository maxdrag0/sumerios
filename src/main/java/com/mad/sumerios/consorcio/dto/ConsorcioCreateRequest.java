package com.mad.sumerios.consorcio.dto;

import com.mad.sumerios.expensa.dto.ExpensaCreateDTO;
import lombok.Data;

@Data
public class ConsorcioCreateRequest {
    private ConsorcioCreateDTO consorcioCreateDTO;
    private ExpensaCreateDTO expensaCreateDTO;
}
