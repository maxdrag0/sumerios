package com.mad.sumerios.movimientos.pagouf.dto;

import lombok.Data;

import java.util.List;

@Data
public class PagoUFRequest {
    private Boolean imprimir;
    private Boolean enviarMail;
    private List<String> mails;
    private PagoUFCreateDTO pago;
}
