package com.mad.sumerios.administracion.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_administracion")
public class Administracion {
    //    ADMINISTRACION
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
    @Email(message = "Debe ser un correo electrónico válido")
    private String mail;

    //    ADMINISTRADOR
    @NotBlank
    private String nombreAdministrador;
    private String matriculaAdministrador;


    //    CONSORCIOS
    @OneToMany(mappedBy = "administracion",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Consorcio> consorcios;
}
