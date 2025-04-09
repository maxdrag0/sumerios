package com.mad.sumerios.enums;


import lombok.Getter;

@Getter
public enum TipoEgreso {
    REMUNERACION_AL_PERSONAL("Remuneración al personal"),
    CARGAS_SOCIALES("Cargas sociales"),
    SERVICIOS_PUBLICOS("Servicios públicos"),
    ABONOS_SERVICIOS("Abonos de servicios"),
    MANTENIMIENTO("Mantenimiento de partes comunes"),
    TRABAJO_EN_UNIDAD("Trabajo de reparaciones en unidades"),
    GASTOS_BANCARIOS("Gastos bancarios"),
    GASTOS_DE_LIMPIEZA("Gastos de limpieza"),
    GASTOS_ADM("Gastos de administración"),
    SEGURO("Pago de seguro"),
    OTROS("Otros"),
    GASTOS_PARTICULARES("Gastos particulares"),
    FONDO_ADM("Fondo de administración");

    private final String descripcion;

    TipoEgreso(String descripcion) {
        this.descripcion = descripcion;
    }

}

