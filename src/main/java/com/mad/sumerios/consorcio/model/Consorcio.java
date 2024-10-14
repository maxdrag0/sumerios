package com.mad.sumerios.consorcio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.pendientes.expensa.model.Expensa;
//import com.mad.sumerios.movimientos.egreso.model.Egreso;
//import com.mad.sumerios.pendientes.movimientos.ingreso.model.Ingreso;
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

    private String titulo;
    private String cbu;
    private String banco;
    private String numCuenta;
    private String alias;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adm")
    @JsonBackReference
    private Administracion administracion;

    @OneToMany(mappedBy = "consorcio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<UnidadFuncional> unidadesFuncionales;

//    @OneToMany(mappedBy = "consorcio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference
//    private List<Expensa> expensas;
}
