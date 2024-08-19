package com.mad.sumerios.enums;

public enum FormaPago {

    EFECTIVO("Efectivo"),
    DEPOSITO("Deposito"),
    TRANSFERENCIA("Transferencia");

    private final String descripcion;

    FormaPago(String descripcion) {
        this.descripcion = descripcion;
    }
}
