package com.mad.sumerios.administracion.service;

import com.mad.sumerios.administracion.dto.AdministracionRegisterDTO;
import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.repository.IAdministracionRepository;
import com.mad.sumerios.appuseradmin.dto.AppUserAdminRegisterDTO;
import com.mad.sumerios.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.appuseradmin.repository.IAppUserAdminRepository;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministracionService {

    private final IAdministracionRepository administracionRepository;
    private final IAppUserAdminRepository appUserAdminRepository;


    @Autowired
    public AdministracionService(IAdministracionRepository administracionRepository, IAppUserAdminRepository appUserAdminRepository) {
        this.administracionRepository = administracionRepository;
        this.appUserAdminRepository = appUserAdminRepository;
    }

    // CREACIÓN DE ADMINISTRACION
    public void createAdministracion(AdministracionRegisterDTO dto) throws Exception {
        Administracion administracion = mapToEntity(dto);
        validateAdministracion(administracion);
        administracionRepository.save(administracion);
    }

    // OBTENER TODAS LAS ADMINISTRACIONES
    public List<AdministracionResponseDTO> getAdministraciones() {
        List<Administracion> administraciones = administracionRepository.findAll();
        return administraciones.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // OBTENER ADMINISTRACION POR ID
    public AdministracionResponseDTO getAdministracionById(Long idAdm) throws Exception {
        Administracion administracion = administracionRepository.findByIdAdm(idAdm)
                .orElseThrow(() -> new Exception("Administración no encontrada"));
        return mapToResponseDTO(administracion);
    }

    // ACTUALIZAR ADMINISTRACION
    public void updateAdministracion(Long idAdm, AdministracionRegisterDTO dto) throws Exception {
        Administracion existingAdm = administracionRepository.findByIdAdm(idAdm)
                .orElseThrow(() -> new Exception("Administración no encontrada"));

        Administracion updatedAdm = mapToEntity(dto);
        validateAdministracionOnUpdate(updatedAdm, idAdm);

        // Actualizar campos
        existingAdm.setNombre(updatedAdm.getNombre());
        existingAdm.setDireccion(updatedAdm.getDireccion());
        existingAdm.setTelefono(updatedAdm.getTelefono());
        existingAdm.setCuit(updatedAdm.getCuit());
        existingAdm.setMail(updatedAdm.getMail());
        existingAdm.setAdministrador(updatedAdm.getAdministrador());

        administracionRepository.save(existingAdm);
    }

    // ELIMINAR ADMINISTRACION
    public void deleteAdministracion(Long idAdm) throws Exception {
        Administracion administracion = administracionRepository.findByIdAdm(idAdm)
                .orElseThrow(() -> new Exception("Administración no encontrada"));
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
    private Administracion mapToEntity(AdministracionRegisterDTO dto) throws Exception {
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
        }

        administracion.setAdministrador(administrador);
        return administracion;
    }

    // Mapper de entidad a DTO
    private AdministracionResponseDTO mapToResponseDTO(Administracion administracion) {
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
            AppUserAdminRegisterDTO AdminDto = new AppUserAdminRegisterDTO();
            AdminDto.setIdAppUser(administrador.getIdAppUser());
            AdminDto.setNombre(administrador.getNombre());
            AdminDto.setApellido(administrador.getApellido());
            AdminDto.setUsername(administrador.getUsername());
            AdminDto.setMail(administrador.getMail());
            AdminDto.setTelefono(administrador.getTelefono());
            AdminDto.setMatriculaAdministrador(administrador.getMatriculaAdministrador());
            administracionDTO.setAdministrador(AdminDto);
        }

        // Mapear la lista de consorcios usando ConsorcioResponseDTO
        List<ConsorcioResponseDTO> consorcioDTOList = administracion.getConsorcios().stream()
                .map(consorcio -> {
                    ConsorcioResponseDTO consorcioDTO = new ConsorcioResponseDTO();
                    consorcioDTO.setIdConsorcio(consorcio.getIdConsorcio());
                    consorcioDTO.setNombre(consorcio.getNombre());
                    consorcioDTO.setDireccion(consorcio.getDireccion());
                    return consorcioDTO;
                }).collect(Collectors.toList());
        administracionDTO.setConsorcios(consorcioDTOList);

        return administracionDTO;
    }
}
