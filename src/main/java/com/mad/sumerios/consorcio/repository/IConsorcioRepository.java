package com.mad.sumerios.consorcio.repository;


import com.mad.sumerios.consorcio.model.Consorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IConsorcioRepository extends JpaRepository<Consorcio, Long> {
    Optional<Consorcio> findByNombre(String nombre);
    Optional<Consorcio> findByDireccion(String direccion);
}
