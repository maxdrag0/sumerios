package com.mad.sumerios.proveedor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProveedorUpdateDTO {
    @NotBlank
    private long idProveedor;

    private String nombre;
    private String telefono;
    private String descripcion;
    private String cuit;
    private String cbu;
}
