package com.mad.sumerios.appuser.dto;

import lombok.Data;

@Data
public class AppUserDTO {
    private long idAppUser;
    private String nombre;
    private String apellido;
    private String username;
    private String mail;
    private String telefono;
    private String matriculaAdministrador;
}
