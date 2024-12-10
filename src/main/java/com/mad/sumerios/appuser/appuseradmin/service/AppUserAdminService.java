package com.mad.sumerios.appuser.appuseradmin.service;

import com.mad.sumerios.appuser.appuseradmin.dto.AppUserAdminRegisterDTO;
import com.mad.sumerios.appuser.appuseradmin.dto.AppUserAdminResponseDTO;
import com.mad.sumerios.appuser.appuseradmin.dto.AppUserAdminUpdateDTO;
import com.mad.sumerios.appuser.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.appuser.appuseradmin.repository.IAppUserAdminRepository;
import com.mad.sumerios.enums.RolUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserAdminService {

    private final IAppUserAdminRepository appUserAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserAdminService(IAppUserAdminRepository appUserAdminRepository, PasswordEncoder passwordEncoder) {
        this.appUserAdminRepository = appUserAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //    CREATE ADMINISTRADOR
    public void createAdmin(AppUserAdminRegisterDTO dto) throws Exception {
        AppUserAdmin admin = mapToEntity(dto);
        validateAdmin(admin);
        appUserAdminRepository.save(admin);
    }

    // GET BY USERNAME
    public AppUserAdminResponseDTO getAppUserByUsername(String username) throws Exception {
        AppUserAdmin user = appUserAdminRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Usuario no encontrado con el usuario "+ username));

        return mapToDTO(user);
    }


    public void deleteAdministracion (Long id) throws Exception{
        Optional<AppUserAdmin> existingAdmin = appUserAdminRepository.findById(id);
        if(existingAdmin.isPresent()){
            AppUserAdmin admin = existingAdmin.get();

            admin.setAdministracion(null);
            appUserAdminRepository.save(admin);
        } else{
            throw new Exception("Administrador no encontrado");
        }
    }

    //    UPDATE ADMINISTRADOR
    public AppUserAdminResponseDTO updateAdmin(Long id, AppUserAdminUpdateDTO dto) throws Exception {
        Optional<AppUserAdmin> existingAdmin = appUserAdminRepository.findById(id);
        if (existingAdmin.isPresent()) {
            AppUserAdmin admin = existingAdmin.get();
            validateAdminOnUpdate(admin, id);

            admin.setNombre(dto.getNombre());
            admin.setApellido(dto.getApellido());
            admin.setUsername(dto.getUsername());
            admin.setMail(dto.getMail());
            admin.setTelefono(dto.getTelefono());
            admin.setMatriculaAdministrador(dto.getMatriculaAdministrador());

            appUserAdminRepository.save(admin);

            return mapToDTO(admin);
        } else {
            throw new EntityNotFoundException("Admin no encontrado");
        }
    }

    //    OBTENER ADMIN POR MATRICULA
    public AppUserAdminResponseDTO getAdminByMatricula(String matricula) throws Exception {
        AppUserAdmin admin = appUserAdminRepository.findByMatriculaAdministrador(matricula)
                .orElseThrow(() -> new Exception("Administrador no encontrado con la matricula "+ matricula));

        return mapToDTO(admin);
    }

    // VALIDACIONES
    private void validateAdmin (AppUserAdmin admin) throws Exception{
        if(appUserAdminRepository.findByMail(admin.getMail()).isPresent()){
            throw new Exception("El mail "+admin.getMail()+" ya se encuentra registrado");
        } else if (appUserAdminRepository.findByUsername(admin.getUsername()).isPresent()){
            throw new Exception("El usuario "+admin.getUsername()+" ya se encuentra registrado");
        } else if (admin.getRol() == RolUser.VECINO){
            throw new Exception("El Rol no puede ser "+admin.getRol()+" a la hora de crear un administrador");
        }
    }

    private void validateAdminOnUpdate(AppUserAdmin admin, Long idAdmin) throws Exception {
        if (appUserAdminRepository.findByMail(admin.getMail())
                .filter(a -> a.getIdAppUser() != idAdmin).isPresent()) {
            throw new Exception("Mail ya existente");
        } else if (appUserAdminRepository.findByUsername(admin.getUsername())
                .filter(a -> a.getIdAppUser() != idAdmin).isPresent()){
            throw new Exception("El usuario "+admin.getUsername()+" ya se encuentra registrado");
        } else if (admin.getRol() == RolUser.VECINO){
            throw new Exception("No puedes editar un "+admin.getRol());
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

        dto.setIdAppUser(admin.getIdAppUser());
        dto.setNombre(admin.getNombre());
        dto.setApellido(admin.getApellido());
        dto.setMail(admin.getMail());
        dto.setTelefono(admin.getTelefono());
        dto.setRol(admin.getRol());
        dto.setMatriculaAdministrador(admin.getMatriculaAdministrador());
        if(admin.getAdministracion() != null){
            dto.setIdAdm(admin.getAdministracion().getIdAdm());
        }


        return dto;
    }

}
