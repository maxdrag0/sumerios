package com.mad.sumerios.appuser.service;

import com.mad.sumerios.appuser.dto.AppUserDTO;
import com.mad.sumerios.appuser.dto.AppUserRegisterDTO;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.appuser.repository.IAppUserRepository;
import com.mad.sumerios.enums.RolUser;
import com.mad.sumerios.exception.EmailAlreadyExistsException;
import com.mad.sumerios.exception.ResourceNotFoundException;
import com.mad.sumerios.exception.UsernameAlreadyExistsException;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(IAppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTRO DE USUARIO
    public AppUser registerUser(AppUserRegisterDTO appUserRegisterDTO) throws Exception {
        validarMailExistente(appUserRegisterDTO.getMail());
        validarUsernameExistente(appUserRegisterDTO.getUsername());
        if(appUserRegisterDTO.getRolUser() == null){
            throw new IllegalArgumentException("El Rol no puede ser nulo");
        }
        // Convertir DTO a entidad
        AppUser appUser = new AppUser();
        appUser.setNombre(appUserRegisterDTO.getNombre());
        appUser.setApellido(appUserRegisterDTO.getApellido());
        appUser.setUsername(appUserRegisterDTO.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserRegisterDTO.getPassword())); // Encriptar contraseña
        appUser.setRolUser(RolUser.valueOf(appUserRegisterDTO.getRolUser().toUpperCase())); // Conversión a Enum
        appUser.setMail(appUserRegisterDTO.getMail());
        appUser.setTelefono(appUserRegisterDTO.getTelefono());
        appUser.setMatriculaAdministrador(appUserRegisterDTO.getMatriculaAdministrador());

        return appUserRepository.save(appUser);
    }

    // Buscar usuario por ID
    public Optional<AppUser> getAppUserById(Long id) {
        return appUserRepository.findById(id);
    }

    // Buscar usuario por username
    public Optional<AppUser> getAppUserByUsername(String username) throws Exception {
        return appUserRepository.findByUsername(username);
    }

    // Buscar usuario por mail
    public Optional<AppUser> getAppUserByMail(String mail) throws Exception {
        return appUserRepository.findByMail(mail);
    }

    // Implementación de UserDetailsService                                             @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                appUser.get().getUsername(),
                appUser.get().getPassword(),
                new ArrayList<>() // Aquí puedes agregar los roles y permisos del usuario
        );
    }

    public AppUser updateUser(Long id, AppUserDTO appUserDTO) {
        Optional<AppUser> existingUserOptional = appUserRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            AppUser existingUser = existingUserOptional.get();

            // Verificar si el nuevo username ya está en uso
            if (isUsernameTaken(appUserDTO.getUsername(), existingUser.getIdAppUser())) {
                throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso.");
            }

            // Verificar si el nuevo mail ya está en uso
            if (isEmailTaken(appUserDTO.getMail(), existingUser.getIdAppUser())) {
                throw new EmailAlreadyExistsException("El correo electrónico ya está en uso.");
            }

            // Actualizar solo los campos permitidos
            existingUser.setNombre(appUserDTO.getNombre());
            existingUser.setApellido(appUserDTO.getApellido());
            existingUser.setMail(appUserDTO.getMail());
            existingUser.setTelefono(appUserDTO.getTelefono());
            existingUser.setMatriculaAdministrador(appUserDTO.getMatriculaAdministrador());

            // También actualizar el username si se proporciona
            if (appUserDTO.getUsername() != null) {
                existingUser.setUsername(appUserDTO.getUsername());
            }

            return appUserRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
    }

    // Eliminar usuario por ID
    public void deleteUserById(Long id) {
        appUserRepository.deleteById(id);
    }

    // VALIDACIONES
    private void validarMailExistente(String mail) throws Exception {
        if(appUserRepository.findByMail(mail).isPresent()){
            throw new Exception("El mail "+mail+" ya está registrado");
        }
    }

    private void validarUsernameExistente(String username) throws Exception {
        if(appUserRepository.findByMail(username).isPresent()){
            throw new Exception("El usuario "+username+" ya está registrado");
        }
    }

    private boolean isUsernameTaken(String username, Long userId) {
        return appUserRepository.existsByUsernameAndIdAppUserNot(username, userId);
    }

    private boolean isEmailTaken(String email, Long userId) {
        return appUserRepository.existsByMailAndIdAppUserNot(email, userId);
    }
}


