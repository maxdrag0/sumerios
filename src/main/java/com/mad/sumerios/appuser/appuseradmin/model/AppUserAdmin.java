package com.mad.sumerios.appuser.appuseradmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.appuser.model.AppUser;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("ADMIN")
public class AppUserAdmin extends AppUser {

    @OneToOne(mappedBy = "administrador")
    @JsonIgnore
    private Administracion administracion;

    private String matriculaAdministrador;

    private String cuit;

}
