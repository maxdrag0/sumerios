package com.mad.sumerios.enums;

public enum FormaPago {

    EFECTIVO("Efectivo"),
    BANCO("Banco"),
    FONDO_ADM("Fondo de administración");

    private final String descripcion;

    FormaPago(String descripcion) {
        this.descripcion = descripcion;
    }
}
