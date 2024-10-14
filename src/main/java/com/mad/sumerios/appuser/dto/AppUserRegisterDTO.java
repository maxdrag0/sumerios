package com.mad.sumerios.appuser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AppUserRegisterDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String rolUser;

    @NotBlank
    @Email(message = "Debe ser un correo electrónico válido")
    private String mail;

    @NotBlank
    private String telefono;

    private String matriculaAdministrador;
}

