package com.mad.sumerios.appuser.appuservecino.service;

import com.mad.sumerios.appuser.appuseradmin.dto.AppUserAdminResponseDTO;
import com.mad.sumerios.appuser.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.appuser.appuservecino.dto.AppUserVecinoRegisterDTO;
import com.mad.sumerios.appuser.appuservecino.repository.IAppUserVecinoRepository;
import com.mad.sumerios.appuser.appuservecino.model.AppUserVecino;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserVecinoService{

    private final IAppUserVecinoRepository appUserVecinoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserVecinoService(IAppUserVecinoRepository appUserVecinoRepository, PasswordEncoder passwordEncoder) {
        this.appUserVecinoRepository = appUserVecinoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createVecino(AppUserVecinoRegisterDTO dto) throws Exception {
        AppUserVecino vecino = mapToEntity(dto);
        validateVecino(vecino);
        appUserVecinoRepository.save(vecino);
    }


    public AppUserVecino updateVecino(Long id, AppUserVecino updatedVecino) {
        Optional<AppUserVecino> existingVecino = appUserVecinoRepository.findById(id);
        if (existingVecino.isPresent()) {
            AppUserVecino vecino = existingVecino.get();
            vecino.setUsername(updatedVecino.getUsername());
            vecino.setPassword(updatedVecino.getPassword());
            // Actualizar otros campos según sea necesario
            return appUserVecinoRepository.save(vecino);
        } else {
            throw new EntityNotFoundException("Vecino no encontrado");
        }
    }

    public void deleteVecino(Long id) {
        appUserVecinoRepository.deleteById(id);
    }

    public Optional<AppUserVecino> getVecinoById(Long id) {
        return appUserVecinoRepository.findById(id);
    }

    // VALIDACIONES
    private void validateVecino (AppUserVecino vecino) throws Exception{
        if(appUserVecinoRepository.findByMail(vecino.getMail()).isPresent()){
            throw new Exception("El mail "+vecino.getMail()+" ya se encuentra registrado");
        } else if (appUserVecinoRepository.findByUsername(vecino.getUsername()).isPresent()){
            throw new Exception("El usuario "+vecino.getUsername()+" ya se encuentra registrado");
        }
    }

    // MAPEO DE DTOs
    private AppUserVecino mapToEntity(AppUserVecinoRegisterDTO dto){
        AppUserVecino vecino = new AppUserVecino();

        vecino.setNombre(dto.getNombre());
        vecino.setApellido(dto.getApellido());
        vecino.setUsername(dto.getUsername());
        vecino.setPassword(passwordEncoder.encode(dto.getPassword()));
        vecino.setMail(dto.getMail());
        vecino.setTelefono(dto.getTelefono());
        vecino.setRol(dto.getRol());

        return vecino;
    }

    private AppUserAdminResponseDTO mapToDTO(AppUserAdmin admin){
        AppUserAdminResponseDTO dto = new AppUserAdminResponseDTO();

        return dto;
    }
}
