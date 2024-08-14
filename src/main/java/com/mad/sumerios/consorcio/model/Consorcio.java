package com.mad.sumerios.consorcio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.movimientoegreso.model.MovimientoEgreso;
import com.mad.sumerios.movimientoingreso.model.MovimientoIngreso;
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


    //  DATOS BANCARIOS
//  private String cbu;
//  private String banco;

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

    //    MOVIMIENTOS
    @OneToMany(mappedBy = "consorcio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MovimientoEgreso> movimientosEgresos;

    @OneToMany(mappedBy = "consorcio",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<MovimientoIngreso> movimientosIngreso;
}
