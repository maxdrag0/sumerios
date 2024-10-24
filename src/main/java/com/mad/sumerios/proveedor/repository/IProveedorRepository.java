package com.mad.sumerios.proveedor.repository;

import com.mad.sumerios.proveedor.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProveedorRepository extends JpaRepository<Proveedor, Long> {
    Proveedor findByCuit (String cuit);
    Proveedor findByTelefono (String telefono);
}
