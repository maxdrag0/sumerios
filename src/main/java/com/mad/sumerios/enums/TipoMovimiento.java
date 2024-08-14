package com.mad.sumerios.enums;


import lombok.Getter;

@Getter
public enum TipoMovimiento {

    ABONOS_SERVICIOS("Abonos de servicios"),
    CARGAS_SOCIALES("Cargas sociales"),
    FONDO_ADM("Fondo de administración"),
    GASTOS_ADM("Gastos de administración"),
    GASTOS_BANCARIOS("Gastos bancarios"),
    GASTOS_PARTICULARES("Gastos particulares"),
    MANTENIMIENTO("Mantenimiento de partes comunes"),
//    PAGO_EXPENSAS("Pago de expensas"),
    SEGURO("Pago de seguro"),
    SERVICIOS_PUBLICOS("Servicios públicos"),
    OTROS("Otros");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

}

