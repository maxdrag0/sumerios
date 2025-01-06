package com.mad.sumerios.administracion.service;

import com.mad.sumerios.administracion.dto.*;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.repository.IAdministracionRepository;
import com.mad.sumerios.appuser.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.appuser.appuseradmin.repository.IAppUserAdminRepository;
import com.mad.sumerios.appuser.appuseradmin.service.AppUserAdminService;
import com.mad.sumerios.enums.RolUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministracionService {

    private final IAdministracionRepository administracionRepository;
    private final IAppUserAdminRepository appUserAdminRepository;
    private final AppUserAdminService appUserAdminService;

    @Autowired
    public AdministracionService(IAdministracionRepository administracionRepository, IAppUserAdminRepository appUserAdminRepository,AppUserAdminService appUserAdminService) {
        this.administracionRepository = administracionRepository;
        this.appUserAdminRepository = appUserAdminRepository;
        this.appUserAdminService = appUserAdminService;
    }

    // CREATE ADMINISTRACION
    public void createAdministracion(AdministracionRegisterDTO dto) throws Exception {
        Administracion administracion = mapToAdministracionEntity(dto);
        validateAdministracion(administracion);
        administracionRepository.save(administracion);
    }

    // OBTENER TODAS LAS ADMINISTRACIONES
    public List<AdministracionResponseDTO> getAdministraciones() {
        List<Administracion> administraciones = administracionRepository.findAll();
        return administraciones.stream().map(this::mapToAdministracionResponseDTO).collect(Collectors.toList());
    }

    // OBTENER ADMINISTRACION POR ID
    public AdministracionResponseDTO getAdministracionById(Long idAdm) throws Exception {
        Administracion administracion = administracionRepository.findByIdAdm(idAdm)
                .orElseThrow(() -> new Exception("Administración no encontrada"));
        return mapToAdministracionResponseDTO(administracion);
    }

    // ACTUALIZAR ADMINISTRACION
    public void updateAdministracion(Long idAdm, AdministracionUpdateDTO dto) throws Exception {
        Administracion existingAdm = administracionRepository.findByIdAdm(idAdm)
                .orElseThrow(() -> new Exception("Administración no encontrada"));

        Administracion updatedAdm = mapToAdministracionEntityUpdate(dto);
        validateAdministracionOnUpdate(updatedAdm, idAdm);

        // Actualizar campos
        existingAdm.setNombre(updatedAdm.getNombre());
        existingAdm.setDireccion(updatedAdm.getDireccion());
        existingAdm.setTelefono(updatedAdm.getTelefono());
        existingAdm.setCuit(updatedAdm.getCuit());
        existingAdm.setMail(updatedAdm.getMail());

        administracionRepository.save(existingAdm);
    }

    // ELIMINATOR ADMINISTRACION
    public void deleteAdministracion(Long idAdm) throws Exception {
        Administracion administracion = administracionRepository.findByIdAdm(idAdm)
                .orElseThrow(() -> new Exception("Administración no encontrada"));
        appUserAdminService.deleteAdministracion(administracion.getAdministrador().getIdAppUser());

        administracionRepository.delete(administracion);
    }


    // Validaciones
    private void validateAdministracion(Administracion administracion) throws Exception {
        if (administracionRepository.findByMail(administracion.getMail()).isPresent()) {
            throw new Exception("La administración ya está registrada (mail ya existente)");
        }
    }
    private void validateAdministracionOnUpdate(Administracion administracion, Long idAdm) throws Exception {
        if (administracionRepository.findByMail(administracion.getMail())
                .filter(a -> a.getIdAdm() != idAdm).isPresent()) {
            throw new Exception("Mail ya existente");
        }
    }

    // Mapper de DTO a entidad
    private Administracion mapToAdministracionEntity(AdministracionRegisterDTO dto) throws Exception {
        Administracion administracion = new Administracion();
        administracion.setNombre(dto.getNombre());
        administracion.setDireccion(dto.getDireccion());
        administracion.setTelefono(dto.getTelefono());
        administracion.setCuit(dto.getCuit());
        administracion.setMail(dto.getMail());

        // Buscar el AppUser (administrador)
        AppUserAdmin administrador = appUserAdminRepository.findById(dto.getAdministradorId())
                .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado"));
        if (administrador.getAdministracion() != null) {
            throw new Exception("El administrador ya está asignado a otra administración");
        } else if(administrador.getRol() == RolUser.VECINO){
            throw new Exception("El rol del administrador no puede ser "+administrador.getRol());
        }

        administracion.setAdministrador(administrador);
        administrador.setAdministracion(administracion);
        return administracion;
    }

    private Administracion mapToAdministracionEntityUpdate(AdministracionUpdateDTO dto) throws Exception {
        AppUserAdmin administrador = appUserAdminRepository.findById(dto.getIdAppUser())
                .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado"));

        Administracion administracion = new Administracion();
        administracion.setIdAdm(dto.getIdAdm());
        administracion.setNombre(dto.getNombre());
        administracion.setDireccion(dto.getDireccion());
        administracion.setTelefono(dto.getTelefono());
        administracion.setCuit(dto.getCuit());
        administracion.setMail(dto.getMail());

        // Buscar el AppUser (administrador)

        if (administrador.getAdministracion() != null) {
            long idAdmExistente = administrador.getAdministracion().getIdAdm();
            if (idAdmExistente != administracion.getIdAdm()) {
                throw new Exception("El administrador ya está asignado a otra administración");
            }
        } else if (administrador.getRol() == RolUser.VECINO) {
            throw new Exception("El rol del administrador no puede ser " + administrador.getRol());
        }

        return administracion;
    }

    // Mapper de entidad a DTO
    private AdministracionResponseDTO mapToAdministracionResponseDTO(Administracion administracion) {
        AdministracionResponseDTO administracionDTO = new AdministracionResponseDTO();
        administracionDTO.setIdAdm(administracion.getIdAdm());
        administracionDTO.setNombre(administracion.getNombre());
        administracionDTO.setCuit(administracion.getCuit());
        administracionDTO.setMail(administracion.getMail());
        administracionDTO.setTelefono(administracion.getTelefono());
        administracionDTO.setDireccion(administracion.getDireccion());

        // Mapear el administrador usando AppUserAdminDTO
        AppUserAdmin administrador = administracion.getAdministrador();
        if (administrador != null) {
            AdministracionAdmDTO AdminDto = new AdministracionAdmDTO();
            AdminDto.setIdAppUser(administrador.getIdAppUser());
            AdminDto.setNombre(administrador.getNombre());
            AdminDto.setApellido(administrador.getApellido());
            AdminDto.setMatriculaAdministrador(administrador.getMatriculaAdministrador());
            AdminDto.setCuit(administracion.getCuit());
            administracionDTO.setAdministrador(AdminDto);
        }

        // Mapear la lista de consorcios usando ConsorcioResponseDTO
        List<AdministracionConsorcioDTO> consorcioDTOList = administracion.getConsorcios().stream()
                .map(consorcio -> {
                    AdministracionConsorcioDTO consorcioDTO = new AdministracionConsorcioDTO();
                    consorcioDTO.setIdConsorcio(consorcio.getIdConsorcio());
                    consorcioDTO.setNombre(consorcio.getNombre());
                    consorcioDTO.setDireccion(consorcio.getDireccion());
                    return consorcioDTO;
                }).collect(Collectors.toList());
        administracionDTO.setConsorcios(consorcioDTOList);

        return administracionDTO;
    }
}
