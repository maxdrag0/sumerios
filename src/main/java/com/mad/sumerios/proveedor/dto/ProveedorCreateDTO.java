package com.mad.sumerios.proveedor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProveedorCreateDTO {
    @NotBlank
    private String nombre;

    @NotBlank
    private String telefono;

    private String descripcion;
    private String cuit;
    private String cbu;
}
