package com.mad.sumerios.appuser.appuseradmin.dto;

import com.mad.sumerios.appuser.dto.AppUserResponseDTO;
import lombok.Data;

@Data
public class AppUserAdminResponseDTO extends AppUserResponseDTO {
    private Long idAdm;
    private String matriculaAdministrador;
    private String cuit;
}
