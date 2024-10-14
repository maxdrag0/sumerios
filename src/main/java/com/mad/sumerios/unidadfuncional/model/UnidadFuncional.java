package com.mad.sumerios.unidadfuncional.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.pendientes.movimientos.pagouf.model.PagoUF;
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

    @NotNull
    private String apellidoPropietario;
    @NotNull
    private String nombrePropietario;
    private String mailPropietario;
    private String telefonoPropietario;

    private String apellidoInquilino;
    private String nombreInquilino;
    private String mailInquilino;
    private String telefonoInquilino;

    @NotNull
    private Double deuda;

    private Double intereses;

    @NotNull
    private Double totalA;
    @NotNull
    private Double totalB;
    @NotNull
    private Double totalC;
    @NotNull
    private Double totalD;
    @NotNull
    private Double totalE;

//    @OneToMany(mappedBy = "unidadFuncional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<PagoUF> pagoUFS;

    @ManyToMany(mappedBy = "unidadesFuncionales")
    @JsonBackReference
    private List<AppUser> appUsers;
}
