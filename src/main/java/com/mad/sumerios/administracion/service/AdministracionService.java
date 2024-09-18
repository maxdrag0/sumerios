package com.mad.sumerios.administracion.service;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.repository.IAdministracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministracionService {
    private final IAdministracionRepository administracionRepository;

    @Autowired
    public AdministracionService(IAdministracionRepository administracionRepository) {
        this.administracionRepository = administracionRepository;
    }

    public void createAdministracion(Administracion administracion) throws Exception {
        validateAdministracion(administracion);
        administracionRepository.save(administracion);
    }

    public List<Administracion> getAdministraciones() {
        return administracionRepository.findAll();
    }

    public Optional<Administracion> getAdministracionById(Long id) {
        return administracionRepository.findById(id);
    }

    public void updateAdministracion(Administracion administracion) throws Exception {
        Administracion existingAdm = administracionRepository.findById(administracion.getIdAdm())
                .orElseThrow(() -> new Exception("Administración no encontrada"));

        // Valida datos
        validateAdministracionOnUpdate(administracion);

        // Actualizar solo los campos que no afectan la lista consorcios
        existingAdm.setNombre(administracion.getNombre());
        existingAdm.setDireccion(administracion.getDireccion());
        existingAdm.setTelefono(administracion.getTelefono());
        existingAdm.setCuit(administracion.getCuit());
        existingAdm.setMail(administracion.getMail());
        existingAdm.setNombreAdministrador(administracion.getNombreAdministrador());
        existingAdm.setMatriculaAdministrador(administracion.getMatriculaAdministrador());

        // Guardar la entidad actualizada
        administracionRepository.save(existingAdm);
    }

    public void deleteAdministracion(Long id) throws Exception {
        Administracion adm = administracionRepository.findById(id)
                .orElseThrow(() -> new Exception("Administración no encontrada"));
        administracionRepository.delete(adm);
    }

    //validaciones
    private void validateAdministracion(Administracion administracion) throws Exception {
        if (administracionRepository.findByNombre(administracion.getNombre()).isPresent()) {
            throw new Exception("La administración ya está registrada (nombre ya existente)");
        } else if (administracionRepository.findByMail(administracion.getMail()).isPresent()) {
            throw new Exception("La administración ya está registrada (mail ya existente)");
        } else if (administracionRepository.findByNombreAdministrador(administracion.getNombreAdministrador()).isPresent() &&
                administracionRepository.findByMatriculaAdministrador(administracion.getMatriculaAdministrador()).isPresent()) {
            throw new Exception("Un administrador no puede tener más de una administración.");
        }
    }

    private void validateAdministracionOnUpdate(Administracion administracion) throws Exception {
        if (administracionRepository.findByNombre(administracion.getNombre())
                .filter(a -> a.getIdAdm() != administracion.getIdAdm()).isPresent()) {
            throw new Exception("Nombre ya existente");
        } else if (administracionRepository.findByMail(administracion.getMail())
                .filter(a -> a.getIdAdm() != administracion.getIdAdm()).isPresent()) {
            throw new Exception("Mail ya existente");
        }
    }
}
