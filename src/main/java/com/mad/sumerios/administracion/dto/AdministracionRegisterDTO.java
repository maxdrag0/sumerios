package com.mad.sumerios.administracion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdministracionRegisterDTO {
    @NotBlank
    private String nombre;

    @NotBlank
    private String direccion;

    @NotBlank
    private String telefono;

    @NotBlank
    private String cuit;

    @NotBlank
    @Email(message = "Debe ser un correo electrónico válido")
    private String mail;

    @NotNull
    private Long administradorId; // ID del AppUser que es administrador
}
