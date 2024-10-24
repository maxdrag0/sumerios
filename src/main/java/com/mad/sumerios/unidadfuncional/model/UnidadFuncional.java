package com.mad.sumerios.unidadfuncional.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mad.sumerios.appuser.appuservecino.model.AppUserVecino;
import com.mad.sumerios.consorcio.model.Consorcio;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_unidad_funcional")
public class UnidadFuncional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uf")
    private long idUf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @NotNull
    @JsonBackReference
    private Consorcio consorcio;

    @NotNull
    @Min(1)
    private int unidadFuncional;

    @NotBlank
    @Size(max = 5)
    private String titulo;

    @NotNull
    @Min(0)
    private Double porcentajeUnidad;

    // DATOS PERSONAS
    // PROPIETARIO
    @NotNull
    private String apellidoPropietario;
    @NotNull
    private String nombrePropietario;
    private String mailPropietario;
    private String telefonoPropietario;
    // INQUILINO
    private String apellidoInquilino;
    private String nombreInquilino;
    private String mailInquilino;
    private String telefonoInquilino;

    // ESTADO DE CUENTA
    private Double deuda;
    private Double intereses;
    private Double totalA;
    private Double totalB;
    private Double totalC;
    private Double totalD;
    private Double totalE;
    private Double gastoParticular;
    private Double totalFinal;

//    @OneToMany(mappedBy = "unidadFuncional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<PagoUF> pagoUFS;

    @ManyToMany(mappedBy = "unidadesFuncionales")
    @JsonBackReference
    private List<AppUserVecino> appUsers;
}
