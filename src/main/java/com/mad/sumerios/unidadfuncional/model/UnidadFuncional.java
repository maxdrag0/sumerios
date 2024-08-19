package com.mad.sumerios.unidadfuncional.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.ingreso.model.Ingreso;
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

    //  UNIDAD FUNCIONAL
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uf")
    private long idUf;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consorcio")
    @NotNull
    @JsonBackReference
    private Consorcio consorcio;

    @NotNull
    @Min(1)
    private int unidadFuncional;

    @NotBlank
    @Size(max = 3)
    private String numeroUnidad;

    @NotBlank
    @Size(max = 4)
    private String letraUnidad;

    @NotNull
    @Min(0)
    private Double porcentajeUnidad;

    //  ESTADO DE CUENTA Y PAGOS
    private Double deuda;

    @OneToMany(mappedBy = "unidadFuncional",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ingreso> ingresos;

    //  DATOS PROPIETARIO
    @NotNull
    private String apellidoPropietario;
    @NotNull
    private String nombrePropietario;
    private String mailPropietario;
    private String telefonoPropietario;

    //  DATOS INQUILINO
    private String apellidoInquilino;
    private String nombreInquilino;
    private String mailInquilino;
    private String telefonoInquilino;

}
