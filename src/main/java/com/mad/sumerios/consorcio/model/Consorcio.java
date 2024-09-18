package com.mad.sumerios.consorcio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.expensa.model.Expensa;
//import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
//import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
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

    //  CONSORCIO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consorcio")
    private long idConsorcio;

    @NotBlank
    private String nombre;

    @NotBlank
    private String direccion;

    @NotBlank
    private String ciudad;

    private String cuit;

    //  DATOS BANCARIOS
    private String titulo;
    private String cbu;
    private String banco;
    private String numCuenta;
    private String alias;

    //  ADM DEL CONSORCIO
    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adm")
    @JsonBackReference
    private Administracion administracion;

    //  UNIDADES FUNCIONALES
    @OneToMany(mappedBy = "consorcio",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UnidadFuncional> unidadesFuncionales;

    //    EXPENSAS
    @OneToMany(mappedBy = "consorcio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Expensa> expensas;

    //    MOVIMIENTOS
    @OneToMany(mappedBy = "consorcio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ingreso> ingresos;

//    @OneToMany(mappedBy = "consorcio",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<Egreso> egresos;

//    @OneToMany(mappedBy = "consorcio",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<PagoUF> pagoUFS;
}
