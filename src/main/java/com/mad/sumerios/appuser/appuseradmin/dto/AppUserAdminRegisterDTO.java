package com.mad.sumerios.appuser.appuseradmin.dto;

import com.mad.sumerios.appuser.dto.AppUserRegisterDTO;
import com.mad.sumerios.enums.RolUser;
import lombok.Data;

@Data
public class AppUserAdminRegisterDTO extends AppUserRegisterDTO {
    private String matriculaAdministrador;
    private String cuit;
}

