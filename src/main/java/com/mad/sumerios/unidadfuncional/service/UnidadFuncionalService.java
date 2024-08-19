package com.mad.sumerios.unidadfuncional.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadFuncionalService {

    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final IConsorcioRepository consorcioRepository;

    @Autowired
    public UnidadFuncionalService(IUnidadFuncionalRepository unidadFuncionalRepository, IConsorcioRepository consorcioRepository) {
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.consorcioRepository = consorcioRepository;
    }

    //  CREAR UNIDAD FUNCIONAL
    public void createUnidadFuncional(UnidadFuncional unidadFuncional) throws Exception{
        Optional<UnidadFuncional> existente = unidadFuncionalRepository.findByNumeroYLetraEnConsorcio(
                unidadFuncional.getConsorcio(),
                unidadFuncional.getNumeroUnidad(),
                unidadFuncional.getLetraUnidad());
        if(existente.isPresent()){
            throw new Exception(
                "Unidad funcional "+unidadFuncional.getNumeroUnidad()+unidadFuncional.getLetraUnidad()+" ya existe");
        }

        unidadFuncionalRepository.save(unidadFuncional);
    }

    //  LISTAR UF
    public List<UnidadFuncional> getUnidades(){
        return unidadFuncionalRepository.findAll();
    }

    public List<UnidadFuncional> getUnidadesFuncionalesPorConsorcio(Long idConsorcio) throws Exception {
        Consorcio consorcio = consorcioRepository.findById(idConsorcio)
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));

        return unidadFuncionalRepository.findByConsorcio(consorcio);
    }

    //  ACTUALIZAR UF
    public void updateUnidadFuncional(UnidadFuncional unidadFuncional) throws Exception{
        UnidadFuncional uf = unidadFuncionalRepository.findById(unidadFuncional.getIdUf())
                .orElseThrow(() -> new Exception("Unidad Funcional no encontrada"));

//      DATOS UF
        uf.setNumeroUnidad(unidadFuncional.getNumeroUnidad());
        uf.setLetraUnidad(unidadFuncional.getLetraUnidad());
        uf.setPorcentajeUnidad(unidadFuncional.getPorcentajeUnidad());
//      DATOS PROP
        uf.setApellidoPropietario(unidadFuncional.getApellidoPropietario());
        uf.setNombrePropietario(unidadFuncional.getNombrePropietario());
        uf.setMailPropietario(unidadFuncional.getMailPropietario());
        uf.setTelefonoPropietario(unidadFuncional.getTelefonoPropietario());
//      DATOS INQ
        uf.setApellidoInquilino(unidadFuncional.getApellidoInquilino());
        uf.setNombreInquilino(unidadFuncional.getNombreInquilino());
        uf.setMailInquilino(unidadFuncional.getMailInquilino());
        uf.setTelefonoInquilino(unidadFuncional.getTelefonoInquilino());

        unidadFuncionalRepository.save(uf);
    }

    //  ELIMINAR UF
    public void deleteUnidadFuncional(Long id) throws Exception{
        UnidadFuncional uf = unidadFuncionalRepository.findById(id)
                .orElseThrow(() -> new Exception("Unidad Funcional no encontrada"));

        unidadFuncionalRepository.delete(uf);
    }
}
