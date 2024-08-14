package com.mad.sumerios.administracion.service;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.repository.IAdministracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministracionService {
    private final IAdministracionRepository administracionRepository;

    @Autowired
    public AdministracionService(IAdministracionRepository administracionRepository){
        this.administracionRepository = administracionRepository;
    }

    //  CREAR ADMINISTRACION
    public void createAdministracion(Administracion administracion) throws Exception{
        if (administracionRepository.findByNombre(administracion.getNombre()).isPresent()) {
            throw new Exception("La administración ya está registrada (nombre ya existente)");
        } else if(administracionRepository.findByMail(administracion.getMail()).isPresent()){
            throw new Exception("La administración ya está registrada (mail ya existente)");
        } else if(
           administracionRepository.findByNombreAdministrador(administracion.getNombreAdministrador()).isPresent() &&
           administracionRepository.findByMatriculaAdministrador(administracion.getMatriculaAdministrador()).isPresent()
        ){
            throw new Exception("Un administrador no puede tener mas de una administración!");
        }
        this.administracionRepository.save(administracion);
    }

    //  LISTAR ADMINISTRACIONES
    public List<Administracion> getAdministraciones(){
        return administracionRepository.findAll();
    }

    // ACTUALIZAR ADMINISTRACION
    public void updateAdministracion(Administracion administracion) throws Exception {
        Administracion adm = administracionRepository.findById(administracion.getIdAdm())
                .orElseThrow(() -> new Exception("Administración no encontrada"));

        if (administracionRepository.findByNombre(administracion.getNombre())
                .filter(a -> a.getIdAdm() != administracion.getIdAdm()).isPresent()) {
            throw new Exception("La administración ya está registrada\nNombre: "+administracion.getNombre()+" ya existente");
        } else if(administracionRepository.findByMail(administracion.getMail())
                .filter(a -> a.getIdAdm() != administracion.getIdAdm()).isPresent()){
            throw new Exception("La administración ya está registrada\nMail: "+administracion.getMail()+" ya existente");
        }

        adm.setNombre(administracion.getNombre());
        adm.setDireccion(administracion.getDireccion());
        adm.setTelefono(administracion.getTelefono());
        adm.setMail(administracion.getMail());
        adm.setNombreAdministrador(administracion.getNombreAdministrador());
        adm.setMatriculaAdministrador(administracion.getMatriculaAdministrador());

        administracionRepository.save(adm);
    }

    //  ELIMINAR ADMINISTRACION
    public Administracion deleteAdministracion(Long id) throws Exception{
        Administracion adm = administracionRepository.findById(id)
                .orElseThrow(() -> new Exception("Administración no encontrada"));

        administracionRepository.delete(adm);
        return adm;
    }

    // BUSQUEDA
}
