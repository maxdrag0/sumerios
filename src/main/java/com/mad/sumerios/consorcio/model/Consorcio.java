package com.mad.sumerios.consorcio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import com.mad.sumerios.expensa.model.Expensa;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_consorcio")
public class Consorcio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consorcio")
    private long idConsorcio;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adm")
    @JsonBackReference
    private Administracion administracion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_estado_cuenta_consorcio", referencedColumnName = "id_estado_cuenta_consorcio")
    private EstadoCuentaConsorcio estadoCuentaConsorcio;

    @NotBlank
    private String nombre;
    @NotBlank
    private String direccion;
    @NotBlank
    private String ciudad;

    private String cuit;
    private String titulo;
    private String cbu;
    private String banco;
    private String numCuenta;
    private String alias;

    @OneToMany(mappedBy = "consorcio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UnidadFuncional> unidadesFuncionales;

    @OneToMany(mappedBy = "consorcio",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Expensa> expensas;

}
