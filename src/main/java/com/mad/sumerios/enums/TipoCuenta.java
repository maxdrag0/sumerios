package com.mad.sumerios.enums;

public enum TipoCuenta {
    CUENTA_CORRIENTE("Cuenta corriente"),
    CAJA_AHORRO("Caja de ahorro");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }
}
