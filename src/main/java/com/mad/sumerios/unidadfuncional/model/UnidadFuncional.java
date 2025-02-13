package com.mad.sumerios.unidadfuncional.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.appuser.appuservecino.model.AppUserVecino;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
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
    @JsonIgnore
    private Consorcio consorcio;

    @NotNull
    @Min(0)
    private int unidadFuncional;

    @NotBlank
    private String titulo;

    @NotNull
    @Min(0)
    private Double porcentajeUnidad;
    @NotNull
    @Min(0)
    private Double porcentajeUnidadB;
    @NotNull
    @Min(0)
    private Double porcentajeUnidadC;
    @NotNull
    @Min(0)
    private Double porcentajeUnidadD;
    @NotNull
    @Min(0)
    private Double porcentajeUnidadE;

    // DATOS PERSONAS
    // PROPIETARIO
    @NotNull
    private String apellidoPropietario;
    private String nombrePropietario;
    private String mailPropietario;
    //    String[] correos = mailPropietario.split(",");
    //    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, orphanRemoval = true)
    //    private List<MailPropietario> mails = new ArrayList<>();
    private String telefonoPropietario;
    // INQUILINO
    private String apellidoInquilino;
    private String nombreInquilino;
    private String mailInquilino;

    private String telefonoInquilino;

    // ESTADO DE CUENTA
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_cuenta_uf")
    @JsonIgnore
    private EstadoCuentaUf estadoCuentaUf;

//    @ManyToMany(mappedBy = "unidadesFuncionales")
//    @JsonBackReference
//    private List<AppUserVecino> appUsers;

}
