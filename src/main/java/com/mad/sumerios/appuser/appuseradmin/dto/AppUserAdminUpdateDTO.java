package com.mad.sumerios.appuser.appuseradmin.dto;

import com.mad.sumerios.appuser.dto.AppUserUpdateDTO;
import lombok.Data;

@Data
public class AppUserAdminUpdateDTO extends AppUserUpdateDTO {
    String matriculaAdministrador;
}
