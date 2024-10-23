package com.mad.sumerios.unidadfuncional.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.unidadfuncional.dto.UfConsorcioDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalCreateDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalUpdateDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void createUnidadFuncional(Long idConsorcio, UnidadFuncionalCreateDTO dto) throws Exception {
        if(consorcioRepository.findById(idConsorcio).isEmpty()){
            throw new Exception("Consorcio no encontrado");
        }

        verificarTituloUnico(dto.getTitulo(), idConsorcio);
        verificarNumeroUnidadUnico(dto.getUnidadFuncional(),idConsorcio);

        unidadFuncionalRepository.save(mapToUnidadFuncionalEntity(dto));
    }

    //  LISTAR UF
    public List<UnidadFuncionalResponseDTO> getUnidadesPorConsorcio(Long idConsorcio) {
        List<UnidadFuncional> ufs = unidadFuncionalRepository.findByConsorcio_IdConsorcio(idConsorcio);
        return ufs.stream().map(this::mapToUnidadFuncionalResponseDTO).collect(Collectors.toList());
    }

    // get by id
    public UnidadFuncionalResponseDTO getUnidadFuncionalById(Long idUf) throws Exception {
        UnidadFuncional uf = unidadFuncionalRepository.findById(idUf)
                .orElseThrow(()-> new Exception("Unidad Funcional no encontrada"));

        return mapToUnidadFuncionalResponseDTO(uf);
    }

    //  ACTUALIZAR UF
    public void updateUnidadFuncional(Long idConsorcio, Long idUnidadFuncional, UnidadFuncionalUpdateDTO dto) throws Exception{
        // Verifica si la unidad funcional existe
        UnidadFuncional ufExistente = unidadFuncionalRepository.findById(idUnidadFuncional)
                .orElseThrow(() -> new Exception("Unidad Funcional no encontrada"));

        // Verifica que la unidad funcional pertenece al consorcio correcto
        if (ufExistente.getConsorcio().getIdConsorcio() != idConsorcio) {
            throw new Exception("La Unidad Funcional no pertenece al Consorcio especificado");
        }

        // Validaciones
        verificarTituloUnicoUpdate(idUnidadFuncional,dto.getTitulo(),idConsorcio);
        verificarNumeroUnidadUnicoUpdate(idUnidadFuncional,dto.getUnidadFuncional(),idConsorcio);
        // Mapeo a entidad
        UnidadFuncional ufUpdated = mapToUnidadFuncionalEntityUpdate(dto);

        // DATOS UF
        ufExistente.setUnidadFuncional(ufUpdated.getUnidadFuncional());
        ufExistente.setTitulo(ufUpdated.getTitulo());
        ufExistente.setPorcentajeUnidad(ufUpdated.getPorcentajeUnidad());
        ufExistente.setDeuda(ufUpdated.getDeuda());
        // DATOS PROP
        ufExistente.setApellidoPropietario(ufUpdated.getApellidoPropietario());
        ufExistente.setNombrePropietario(ufUpdated.getNombrePropietario());
        ufExistente.setMailPropietario(ufUpdated.getMailPropietario());
        ufExistente.setTelefonoPropietario(ufUpdated.getTelefonoPropietario());
        // DATOS INQ
        ufExistente.setApellidoInquilino(ufUpdated.getApellidoInquilino());
        ufExistente.setNombreInquilino(ufUpdated.getNombreInquilino());
        ufExistente.setMailInquilino(ufUpdated.getMailInquilino());
        ufExistente.setTelefonoInquilino(ufUpdated.getTelefonoInquilino());
        // DATOS DE CUENTA
        ufExistente.setDeuda(ufUpdated.getDeuda());
        ufExistente.setIntereses(ufUpdated.getIntereses());
        ufExistente.setTotalA(ufUpdated.getTotalA());
        ufExistente.setTotalB(ufUpdated.getTotalB());
        ufExistente.setTotalC(ufUpdated.getTotalC());
        ufExistente.setTotalD(ufUpdated.getTotalD());
        ufExistente.setTotalE(ufUpdated.getTotalE());
        ufExistente.setGastoParticular(ufUpdated.getGastoParticular());
        ufExistente.setTotalFinal(ufUpdated.getTotalFinal());

        unidadFuncionalRepository.save(ufExistente);
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

    //  VALIDACIONES CREATE
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

    //  VALIDACIONES UPDATE
    private void verificarTituloUnicoUpdate(Long idUf, String titulo, Long idConsorcio) throws Exception {
        Optional<UnidadFuncional> existenteTitulo =
                unidadFuncionalRepository.findByTituloAndConsorcio_IdConsorcio(titulo, idConsorcio);

        if (existenteTitulo.isPresent() && existenteTitulo.get().getIdUf() != idUf) {
            throw new Exception("Unidad funcional con el título '" + titulo + "' ya existe en el consorcio");
        }
    }
    private void verificarNumeroUnidadUnicoUpdate(Long idUf, int unidadFuncional, Long idConsorcio) throws Exception {
        Optional<UnidadFuncional> existenteUF =
                unidadFuncionalRepository.findByUnidadFuncionalAndConsorcio_IdConsorcio(unidadFuncional, idConsorcio);

        if (existenteUF.isPresent() && existenteUF.get().getIdUf() != idUf) {
            throw new Exception("Unidad funcional N°" + unidadFuncional + " ya existe en el consorcio");
        }
    }


    //  MAPEO A DTO
    private UnidadFuncional mapToUnidadFuncionalEntity(UnidadFuncionalCreateDTO dto) throws Exception{
        UnidadFuncional uf = new UnidadFuncional();

        Optional<Consorcio> consorcio = consorcioRepository.findById(dto.getIdConsorcio());
        if(consorcio.isEmpty()){
            throw new Exception("Consorcio no encontrado");
        }

        uf.setConsorcio(consorcio.get());
        uf.setUnidadFuncional(dto.getUnidadFuncional());
        uf.setTitulo(dto.getTitulo());
        uf.setPorcentajeUnidad(dto.getPorcentajeUnidad());

        uf.setApellidoPropietario(dto.getApellidoPropietario());
        uf.setNombrePropietario(dto.getNombrePropietario());
        uf.setMailPropietario(dto.getMailPropietario());
        uf.setTelefonoPropietario(dto.getTelefonoPropietario());

        uf.setApellidoInquilino(dto.getApellidoInquilino());
        uf.setNombreInquilino(dto.getNombreInquilino());
        uf.setMailInquilino(dto.getMailInquilino());
        uf.setTelefonoInquilino(dto.getTelefonoInquilino());

        uf.setDeuda(dto.getDeuda());
        uf.setIntereses(dto.getIntereses());
        uf.setTotalA(dto.getTotalA());
        uf.setTotalB(dto.getTotalB());
        uf.setTotalC(dto.getTotalC());
        uf.setTotalD(dto.getTotalD());
        uf.setTotalE(dto.getTotalE());
        uf.setGastoParticular(dto.getGastoParticular());
        uf.setTotalFinal(dto.getTotalFinal());

        return uf;
    }
    private UnidadFuncionalResponseDTO mapToUnidadFuncionalResponseDTO(UnidadFuncional uf){
        UnidadFuncionalResponseDTO dto = new UnidadFuncionalResponseDTO();

        dto.setIdUf(uf.getIdUf());
        dto.setUnidadFuncional(uf.getUnidadFuncional());
        dto.setTitulo(uf.getTitulo());
        dto.setApellidoPropietario(uf.getApellidoPropietario());
        dto.setNombrePropietario(uf.getNombrePropietario());
        dto.setDeuda(uf.getDeuda());

        Consorcio cons = uf.getConsorcio();
        if(cons != null){
            UfConsorcioDTO consDTO = new UfConsorcioDTO();
            consDTO.setIdConsorcio(cons.getIdConsorcio());
            consDTO.setNombre(cons.getNombre());
            consDTO.setDireccion(consDTO.getDireccion());

            dto.setConsorcioDTO(consDTO);
        }

        return dto;
    }
    private UnidadFuncional mapToUnidadFuncionalEntityUpdate(UnidadFuncionalUpdateDTO dto)throws Exception{
        UnidadFuncional uf = new UnidadFuncional();
        Optional<Consorcio> cons = consorcioRepository.findById(dto.getIdConsorcio());
        if(cons.isEmpty()){
            throw new Exception("Consorcio no encontrado");
        }

        uf.setIdUf(dto.getIdUf());
        uf.setConsorcio(cons.get());
        uf.setUnidadFuncional(dto.getUnidadFuncional());
        uf.setTitulo(dto.getTitulo());
        uf.setPorcentajeUnidad(dto.getPorcentajeUnidad());

        uf.setApellidoPropietario(dto.getApellidoPropietario());
        uf.setNombrePropietario(dto.getNombrePropietario());
        uf.setMailPropietario(dto.getMailPropietario());
        uf.setTelefonoPropietario(dto.getTelefonoPropietario());

        uf.setApellidoInquilino(dto.getApellidoInquilino());
        uf.setNombreInquilino(dto.getNombreInquilino());
        uf.setMailInquilino(dto.getMailInquilino());
        uf.setTelefonoInquilino(dto.getTelefonoInquilino());

        uf.setDeuda(dto.getDeuda());
        uf.setIntereses(dto.getIntereses());
        uf.setTotalA(dto.getTotalA());
        uf.setTotalB(dto.getTotalB());
        uf.setTotalC(dto.getTotalC());
        uf.setTotalD(dto.getTotalD());
        uf.setTotalE(dto.getTotalE());
        uf.setGastoParticular(dto.getGastoParticular());
        uf.setTotalFinal(dto.getTotalFinal());


        return uf;
    }

}
