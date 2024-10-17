package com.mad.sumerios.appuservecino.repository;

import com.mad.sumerios.appuservecino.model.AppUserVecino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAppUserVecinoRepository extends JpaRepository<AppUserVecino, Long> {
    Optional<AppUserVecino> findByUsername(String username);
    Optional<AppUserVecino> findByMail(String mail);
}
