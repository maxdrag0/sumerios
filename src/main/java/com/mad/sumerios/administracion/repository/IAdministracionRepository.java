package com.mad.sumerios.administracion.repository;

import com.mad.sumerios.administracion.model.Administracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdministracionRepository extends JpaRepository<Administracion, Long> {
    Optional<Administracion> findByNombre(String nombre);
    Optional<Administracion> findByMail(String mail);
    Optional<Administracion> findByNombreAdministrador(String nombre);
    Optional<Administracion> findByMatriculaAdministrador(String matricula);
}
