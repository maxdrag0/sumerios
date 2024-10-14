package com.mad.sumerios.appuser.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.enums.RolUf;
import com.mad.sumerios.enums.RolUser;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private long idAppUser;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private RolUser rolUser;

    @NotBlank
    @Email(message = "Debe ser un correo electrónico válido")
    private String mail;

    @NotBlank
    private String telefono;

    @OneToOne(mappedBy = "administrador")
    @JsonIgnore
    private Administracion administracion;

    private String matriculaAdministrador;

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
