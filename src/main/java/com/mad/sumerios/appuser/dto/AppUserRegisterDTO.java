package com.mad.sumerios.appuser.dto;

import com.mad.sumerios.enums.RolUser;
import lombok.Data;

@Data
public class AppUserRegisterDTO {
    private long idAppUser;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String mail;
    private String telefono;
    private RolUser rol;
}
