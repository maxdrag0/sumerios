package com.mad.sumerios.appuser.service;

import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.appuser.dto.AppUserResponseDTO;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.appuser.repository.IAppUserRepository;
import com.mad.sumerios.appuseradmin.dto.AppUserAdminRegisterDTO;
import com.mad.sumerios.appuseradmin.dto.AppUserAdminResponseDTO;
import com.mad.sumerios.appuseradmin.model.AppUserAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService implements UserDetailsService {

    private final IAppUserRepository appUserRepository;

    @Autowired
    public AppUserService(IAppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
    }

    // GET BY ID
    public AppUserResponseDTO getAppUserById(Long id) throws Exception {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con el ID: "+ id));

        return mapToDTO(user);
    }

    // GET BY USERNAME
    public AppUserResponseDTO getAppUserByUsername(String username) throws Exception {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Usuario no encontrado con el usuario "+ username));

        return mapToDTO(user);
    }

    // GET BY MAIL
    public AppUserResponseDTO getAppUserByMail(String mail) throws Exception {
        AppUser user = appUserRepository.findByMail(mail)
                .orElseThrow(() -> new Exception("Usuario no encontrado con el usuario "+ mail));

        return mapToDTO(user);
    }

    // OBTENER TODOS LOS USERS
    public List<AppUserResponseDTO> getAllUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return users.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // DELETE USERS
    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    // MAPEO DE DTOs
    private AppUserResponseDTO mapToDTO(AppUser user){
        AppUserResponseDTO dto = new AppUserResponseDTO();

        dto.setIdAppUser(user.getIdAppUser());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setMail(user.getMail());
        dto.setTelefono(user.getTelefono());
        dto.setRol(user.getRol());

        return dto;
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


