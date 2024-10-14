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
    public void createUnidadFuncional(Long idConsorcio, UnidadFuncional unidadFuncional) throws Exception {
        // Obtén el consorcio correspondiente
        Consorcio consorcio = consorcioRepository.findById(idConsorcio)
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));

        // Verifica si el titulo ya existe en el consorcio
        verificarTituloUnico(unidadFuncional.getTitulo(), idConsorcio);

        // Verifica si la unidad funcional ya existe en el consorcio
        verificarNumeroUnidadUnico(unidadFuncional.getUnidadFuncional(),idConsorcio);

        // Guarda la unidad funcional
        unidadFuncionalRepository.save(unidadFuncional);
    }

    //  LISTAR UF
    public List<UnidadFuncional> getUnidadesPorConsorcio(Long idConsorcio) {
        return unidadFuncionalRepository.findByConsorcio_IdConsorcio(idConsorcio);
    }

    //  ACTUALIZAR UF
    public void updateUnidadFuncional(Long idConsorcio, Long idUnidadFuncional, UnidadFuncional unidadFuncional) throws Exception{
        // Verifica si la unidad funcional existe
        UnidadFuncional uf = unidadFuncionalRepository.findById(idUnidadFuncional)
                .orElseThrow(() -> new Exception("Unidad Funcional no encontrada"));

        // Verifica que la unidad funcional pertenece al consorcio correcto
        if (uf.getConsorcio().getIdConsorcio() != idConsorcio) {
            throw new Exception("La Unidad Funcional no pertenece al Consorcio especificado");
        }

//      DATOS UF
        uf.setUnidadFuncional(unidadFuncional.getUnidadFuncional());
        uf.setTitulo(unidadFuncional.getTitulo());
        uf.setPorcentajeUnidad(unidadFuncional.getPorcentajeUnidad());
        uf.setDeuda(unidadFuncional.getDeuda());
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
    public void deleteUnidadFuncional(Long idConsorcio, Long idUnidadFuncional) throws Exception {
        UnidadFuncional uf = unidadFuncionalRepository.findById(idUnidadFuncional)
                .orElseThrow(() -> new Exception("Unidad Funcional no encontrada"));

        // Verifica que la unidad funcional pertenece al consorcio correcto
        if (uf.getConsorcio().getIdConsorcio() != idConsorcio) {
            throw new Exception("La Unidad Funcional no pertenece al Consorcio especificado");
        }

        unidadFuncionalRepository.delete(uf);
    }

    //  VALIDACIONES
    private void verificarTituloUnico(String titulo, Long idConsorcio) throws Exception {
        Optional<UnidadFuncional> existenteTitulo =
                unidadFuncionalRepository.findByTituloAndConsorcio_IdConsorcio(titulo, idConsorcio);
        if (existenteTitulo.isPresent()) {
            throw new Exception("Unidad funcional " + titulo + " ya existe en el consorcio");
        }
    }

    private void verificarNumeroUnidadUnico(int unidadFuncional, Long idConsorcio) throws Exception {
        Optional<UnidadFuncional> existenteUF =
                unidadFuncionalRepository.findByUnidadFuncionalAndConsorcio_IdConsorcio(unidadFuncional, idConsorcio);
        if (existenteUF.isPresent()) {
            throw new Exception("Unidad funcional N°" + unidadFuncional + " ya existe en el consorcio");
        }
    }
}
