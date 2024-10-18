package com.mad.sumerios.appuseradmin.dto;

import com.mad.sumerios.appuser.dto.AppUserResponseDTO;
import lombok.Data;

@Data
public class AppUserAdminResponseDTO extends AppUserResponseDTO {
    private String matriculaAdministrador;
}
