package com.mad.sumerios.appuser.repository;

import com.mad.sumerios.appuser.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByMail(String mail);
    boolean existsByUsernameAndIdAppUserNot(String username, Long userId);
    boolean existsByMailAndIdAppUserNot(String email, Long userId);
}