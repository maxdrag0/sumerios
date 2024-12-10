package com.mad.sumerios.proveedor.repository;

import com.mad.sumerios.proveedor.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByIdAdm (long idAdm);
    Proveedor findByCuit (String cuit);
    Proveedor findByTelefono (String telefono);
}
