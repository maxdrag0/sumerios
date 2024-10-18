package com.mad.sumerios.appuser.service;

import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.appuser.repository.IAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {

    private final IAppUserRepository appUserRepository;

    @Autowired
    public AppUserService(IAppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
    }

    // Buscar usuario por ID
    public Optional<AppUser> getAppUserById(Long id) {
        return appUserRepository.findById(id);
    }

    // Buscar usuario por username
    public Optional<AppUser> getAppUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    // Buscar usuario por mail
    public Optional<AppUser> getAppUserByMail(String mail) {
        return appUserRepository.findByMail(mail);
    }

    // Implementación de UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        System.out.println(appUser);
        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                appUser.get().getUsername(),
                appUser.get().getPassword(),
                new ArrayList<>()
        );
    }
}


