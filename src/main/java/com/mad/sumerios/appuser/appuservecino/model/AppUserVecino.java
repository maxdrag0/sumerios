package com.mad.sumerios.appuser.appuservecino.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.enums.RolUf;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Entity
@DiscriminatorValue("VECINO")
public class AppUserVecino extends AppUser {

    @ManyToMany
    @JoinTable(
            name = "usuario_unidad_funcional",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "unidad_funcional_id")
    )
    @JsonManagedReference
    private List<UnidadFuncional> unidadesFuncionales;

    @ElementCollection
    @CollectionTable(name = "rol_en_unidad_funcional", joinColumns = @JoinColumn(name = "usuario_id"))
    @MapKeyJoinColumn(name = "unidad_funcional_id")
    @Column(name = "rol")
    @JsonBackReference
    private Map<UnidadFuncional, RolUf> rolEnUnidad;

}
