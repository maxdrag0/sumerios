package com.mad.sumerios.proveedor.dto;

import lombok.Data;

@Data
public class ProveedorResponseDTO {
    private Long idProveedor;
    private String nombre;
    private String descripcion;
    private String cuit;
}
