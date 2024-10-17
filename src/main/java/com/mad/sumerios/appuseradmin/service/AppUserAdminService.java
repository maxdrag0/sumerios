package com.mad.sumerios.appuseradmin.service;

import com.mad.sumerios.appuser.dto.AppUserRegisterDTO;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.appuseradmin.dto.AppUserAdminRegisterDTO;
import com.mad.sumerios.appuseradmin.dto.AppUserAdminResponseDTO;
import com.mad.sumerios.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.appuseradmin.repository.IAppUserAdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AppUserAdminService implements UserDetailsService {

    private final IAppUserAdminRepository appUserAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserAdminService(IAppUserAdminRepository appUserAdminRepository, PasswordEncoder passwordEncoder) {
        this.appUserAdminRepository = appUserAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREAR ADMINISTRADOR
    public void createAdmin(AppUserAdminRegisterDTO dto) throws Exception {
        AppUserAdmin admin = mapToEntity(dto);
        validateAdmin(admin);
        appUserAdminRepository.save(admin);
    }

    public AppUserAdmin updateAdmin(Long id, AppUserAdmin updatedAdmin) {
        Optional<AppUserAdmin> existingAdmin = appUserAdminRepository.findById(id);
        if (existingAdmin.isPresent()) {
            AppUserAdmin admin = existingAdmin.get();
            admin.setUsername(updatedAdmin.getUsername());
            admin.setPassword(updatedAdmin.getPassword());
            // Actualizar otros campos según sea necesario
            return appUserAdminRepository.save(admin);
        } else {
            throw new EntityNotFoundException("Admin no encontrado");
        }
    }

    public void deleteAdmin(Long id) {
        appUserAdminRepository.deleteById(id);
    }

    public Optional<AppUserAdmin> getAdminById(Long id) {
        return appUserAdminRepository.findById(id);
    }



    // VALIDACIONES
    private void validateAdmin (AppUserAdmin admin) throws Exception{
        if(appUserAdminRepository.findByMail(admin.getMail()).isPresent()){
            throw new Exception("El mail "+admin.getMail()+" ya se encuentra registrado");
        } else if (appUserAdminRepository.findByUsername(admin.getUsername()).isPresent()){
            throw new Exception("El usuario "+admin.getUsername()+" ya se encuentra registrado");
        }
    }

    // MAPEO DE DTOs
    private AppUserAdmin mapToEntity(AppUserAdminRegisterDTO dto){
        AppUserAdmin admin = new AppUserAdmin();
        admin.setNombre(dto.getNombre());
        admin.setApellido(dto.getApellido());
        admin.setUsername(dto.getUsername());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setMail(dto.getMail());
        admin.setTelefono(dto.getTelefono());
        admin.setRol(dto.getRol());

        admin.setMatriculaAdministrador(dto.getMatriculaAdministrador());


        return admin;
    }

    private AppUserAdminResponseDTO mapToDTO(AppUserAdmin admin){
        AppUserAdminResponseDTO dto = new AppUserAdminResponseDTO();

        return dto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUserAdmin> admin = appUserAdminRepository.findByUsername(username);
        if (admin.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                admin.get().getUsername(),
                admin.get().getPassword(),
                new ArrayList<>()
        );
    }
}
