package com.mad.sumerios.administracion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.appuser.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_administracion")
public class Administracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adm")
    private long idAdm;

    @NotBlank
    private String nombre;

    @NotBlank
    private String direccion;

    @NotBlank
    private String telefono;

    @NotBlank
    private String cuit;

    @NotBlank
    @Email(message = "Debe ser un correo electrónico válido")
    private String mail;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @JsonIgnore
    private AppUserAdmin administrador;

    @OneToMany(mappedBy = "administracion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Consorcio> consorcios;

}
