package com.mad.sumerios.appuser.appuseradmin.dto;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.appuser.dto.AppUserResponseDTO;
import lombok.Data;

@Data
public class AppUserAdminResponseDTO extends AppUserResponseDTO {
    private String matriculaAdministrador;
    private Long administracionId;
}
