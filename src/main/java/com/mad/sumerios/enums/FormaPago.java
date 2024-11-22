package com.mad.sumerios.enums;

public enum FormaPago {

    EFECTIVO("Efectivo"),
    BANCO("Banco");

    private final String descripcion;

    FormaPago(String descripcion) {
        this.descripcion = descripcion;
    }
}
