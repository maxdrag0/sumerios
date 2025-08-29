package com.mad.sumerios.proveedor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProveedorCreateDTO {
    @NotBlank
    private String nombre;

    @NotNull
    private long idAdm;
    private String telefono;

    private String descripcion;
    private String cuit;
    private String cbu;
}
