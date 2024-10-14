package com.mad.sumerios.appuser.dto;

import lombok.Data;

@Data
public class AppUserSearchDTO {

    private String nombre;
    private String apellido;
    private String username;
    private String rolUser;
    private String mail;
    private String telefono;
    private String matriculaAdministrador;
    private String password;
}

