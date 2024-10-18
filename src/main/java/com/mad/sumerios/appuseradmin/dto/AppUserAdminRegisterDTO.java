package com.mad.sumerios.appuseradmin.dto;

import com.mad.sumerios.appuser.dto.AppUserRegisterDTO;
import com.mad.sumerios.enums.RolUser;
import lombok.Data;

@Data
public class AppUserAdminRegisterDTO extends AppUserRegisterDTO {
    private String matriculaAdministrador;
}

