package com.mad.sumerios.pendientes.proveedor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String nombre;

    @NotBlank
    private String telefono;

    @NotBlank
    private String tipoTrabajo;

    private String cuit;
    private String cbu;

}
