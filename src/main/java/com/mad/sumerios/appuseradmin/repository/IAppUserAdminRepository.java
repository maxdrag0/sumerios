package com.mad.sumerios.appuseradmin.repository;

import com.mad.sumerios.appuseradmin.model.AppUserAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAppUserAdminRepository extends JpaRepository<AppUserAdmin, Long> {
    Optional<AppUserAdmin> findByUsername(String username);
    Optional<AppUserAdmin> findByMail(String mail);
    Optional<AppUserAdmin> findByMatriculaAdministrador(String matriculaAdministrador);
}
