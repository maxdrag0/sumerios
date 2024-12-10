package com.mad.sumerios.proveedor.dto;

import lombok.Data;

@Data
public class ProveedorResponseDTO {
    private Long idProveedor;
    private Long idAdm;
    private String nombre;
    private String descripcion;
    private String cuit;
    private String telefono;
    private String cbu;
}
