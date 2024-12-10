package com.mad.sumerios.proveedor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "tbl_proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private long idProveedor;

    @NotNull
    private Long idAdm;

    @NotBlank
    private String nombre;

    @NotBlank
    private String telefono;

    private String descripcion;
    private String cuit;
    private String cbu;

}
