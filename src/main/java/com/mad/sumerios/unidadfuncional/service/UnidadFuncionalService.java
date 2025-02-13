package com.mad.sumerios.unidadfuncional.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfCreateDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.estadocuentauf.service.EstadoCuentaUfService;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalCreateDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalUpdateDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UnidadFuncionalService {

    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final IConsorcioRepository consorcioRepository;
    private final EstadoCuentaUfService estadoCuentaUfService;

    @Autowired
    public UnidadFuncionalService(IUnidadFuncionalRepository unidadFuncionalRepository,
                                  IConsorcioRepository consorcioRepository,
                                  EstadoCuentaUfService estadoCuentaUfService) {
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.consorcioRepository = consorcioRepository;
        this.estadoCuentaUfService = estadoCuentaUfService;
    }

    //  CREAR UNIDAD FUNCIONAL
    @Transactional
    public void createUnidadFuncional(UnidadFuncionalCreateDTO dto) throws Exception {
        UnidadFuncional uf = mapToUnidadFuncionalEntity(dto);
        unidadFuncionalRepository.save(uf);

        EstadoCuentaUfCreateDTO eaDTO = new EstadoCuentaUfCreateDTO(uf.getIdUf(), dto.getPeriodo());
        estadoCuentaUfService.createEstadoCuentaUf(eaDTO);

    }

    @Transactional
    public void createUnidadesFuncionales(List<UnidadFuncionalCreateDTO> dtos) throws Exception{
        for(UnidadFuncionalCreateDTO dto : dtos){
            this.createUnidadFuncional(dto);
        }
    }

    //  LISTAR UF
    public List<UnidadFuncionalResponseDTO> getUnidadesPorConsorcio(Long idConsorcio) {

        List<UnidadFuncional> ufs = unidadFuncionalRepository.findByConsorcio_IdConsorcio(idConsorcio);
        actualizarPorcentajes(ufs);
        return ufs.stream().map(this::mapToUnidadFuncionalResponseDTO).collect(Collectors.toList());
    }


    // get by id
    public UnidadFuncionalResponseDTO getUnidadFuncionalById(Long idUf) throws Exception {
        UnidadFuncional uf = unidadFuncionalRepository.findById(idUf)
                .orElseThrow(()-> new Exception("Unidad Funcional no encontrada"));

        return mapToUnidadFuncionalResponseDTO(uf);
    }

    //  ACTUALIZAR UF
    public void updateUnidadFuncional(Long idConsorcio, Long idUf, UnidadFuncionalUpdateDTO dto) throws Exception{
        // Verifica si la unidad funcional existe
        UnidadFuncional ufExistente = unidadFuncionalRepository.findById(idUf)
                .orElseThrow(() -> new Exception("Unidad Funcional no encontrada"));

        // Verifica que la unidad funcional pertenece al consorcio correcto
        if (ufExistente.getConsorcio().getIdConsorcio() != idConsorcio) {
            throw new Exception("La Unidad Funcional no pertenece al Consorcio especificado");
        }

        // Validaciones
        verificarTituloUnicoUpdate(idUf,dto.getTitulo(),idConsorcio);
        verificarNumeroUnidadUnicoUpdate(idUf,dto.getUnidadFuncional(),idConsorcio);
        // Mapeo a entidad
        UnidadFuncional ufUpdated = mapToUnidadFuncionalEntityUpdate(dto);

        // DATOS UF
        ufExistente.setUnidadFuncional(ufUpdated.getUnidadFuncional());
        ufExistente.setTitulo(ufUpdated.getTitulo());
        ufExistente.setPorcentajeUnidad(ufUpdated.getPorcentajeUnidad());
        ufExistente.setPorcentajeUnidadB(ufUpdated.getPorcentajeUnidadB());
        ufExistente.setPorcentajeUnidadC(ufUpdated.getPorcentajeUnidadC());
        ufExistente.setPorcentajeUnidadD(ufUpdated.getPorcentajeUnidadD());
        ufExistente.setPorcentajeUnidadE(ufUpdated.getPorcentajeUnidadE());

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

        unidadFuncionalRepository.save(ufExistente);
    }

    //  ELIMINAR UF
    public void deleteUnidadFuncional(Long idConsorcio, Long idUf) throws Exception {
        UnidadFuncional uf = unidadFuncionalRepository.findById(idUf)
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
    private Consorcio validateConsorcio(Long idConsorcio) throws Exception {
        Optional<Consorcio> consorcio = consorcioRepository.findById(idConsorcio);
        if(consorcio.isEmpty()){
            throw new Exception("Consorcio no encontrado");
        }
        return consorcio.get();
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
        verificarTituloUnico(dto.getTitulo(), dto.getIdConsorcio());
        verificarNumeroUnidadUnico(dto.getUnidadFuncional(),dto.getIdConsorcio());
        Consorcio consorcio = validateConsorcio(dto.getIdConsorcio());
        UnidadFuncional uf = new UnidadFuncional();

        uf.setConsorcio(consorcio);
        uf.setUnidadFuncional(dto.getUnidadFuncional());
        uf.setTitulo(dto.getTitulo());
        uf.setPorcentajeUnidad(dto.getPorcentajeUnidad());
        uf.setPorcentajeUnidadB(dto.getPorcentajeUnidadB());
        uf.setPorcentajeUnidadC(dto.getPorcentajeUnidadC());
        uf.setPorcentajeUnidadD(dto.getPorcentajeUnidadD());
        uf.setPorcentajeUnidadE(dto.getPorcentajeUnidadE());

        uf.setApellidoPropietario(dto.getApellidoPropietario());
        uf.setNombrePropietario(dto.getNombrePropietario());
        uf.setMailPropietario(dto.getMailPropietario());
        uf.setTelefonoPropietario(dto.getTelefonoPropietario());

        uf.setApellidoInquilino(dto.getApellidoInquilino());
        uf.setNombreInquilino(dto.getNombreInquilino());
        uf.setMailInquilino(dto.getMailInquilino());
        uf.setTelefonoInquilino(dto.getTelefonoInquilino());

        return uf;
    }
    private UnidadFuncional mapToUnidadFuncionalEntityUpdate(UnidadFuncionalUpdateDTO dto)throws Exception{
        UnidadFuncional uf = new UnidadFuncional();
        Consorcio cons = validateConsorcio(dto.getIdConsorcio());

        uf.setIdUf(dto.getIdUf());
        uf.setConsorcio(cons);
        uf.setUnidadFuncional(dto.getUnidadFuncional());
        uf.setTitulo(dto.getTitulo());
        uf.setPorcentajeUnidad(dto.getPorcentajeUnidad());
        uf.setPorcentajeUnidadB(dto.getPorcentajeUnidadB());
        uf.setPorcentajeUnidadC(dto.getPorcentajeUnidadC());
        uf.setPorcentajeUnidadD(dto.getPorcentajeUnidadD());
        uf.setPorcentajeUnidadE(dto.getPorcentajeUnidadE());


        uf.setApellidoPropietario(dto.getApellidoPropietario());
        uf.setNombrePropietario(dto.getNombrePropietario());
        uf.setMailPropietario(dto.getMailPropietario());
        uf.setTelefonoPropietario(dto.getTelefonoPropietario());

        uf.setApellidoInquilino(dto.getApellidoInquilino());
        uf.setNombreInquilino(dto.getNombreInquilino());
        uf.setMailInquilino(dto.getMailInquilino());
        uf.setTelefonoInquilino(dto.getTelefonoInquilino());

        return uf;
    }
    public UnidadFuncionalResponseDTO mapToUnidadFuncionalResponseDTO(UnidadFuncional uf){
        UnidadFuncionalResponseDTO dto = new UnidadFuncionalResponseDTO();

        dto.setIdUf(uf.getIdUf());
        dto.setIdConsorcio(uf.getConsorcio().getIdConsorcio());
        dto.setUnidadFuncional(uf.getUnidadFuncional());
        dto.setTitulo(uf.getTitulo());
        dto.setApellidoPropietario(uf.getApellidoPropietario());
        dto.setNombrePropietario(uf.getNombrePropietario());
        dto.setPorcentajeUnidad(uf.getPorcentajeUnidad());
        dto.setPorcentajeUnidadB(uf.getPorcentajeUnidadB());
        dto.setPorcentajeUnidadC(uf.getPorcentajeUnidadC());
        dto.setPorcentajeUnidadD(uf.getPorcentajeUnidadD());
        dto.setPorcentajeUnidadE(uf.getPorcentajeUnidadE());

        dto.setNombrePropietario(uf.getNombrePropietario());
        dto.setApellidoPropietario(uf.getApellidoPropietario());
        dto.setMailPropietario(uf.getMailPropietario());
        dto.setTelefonoPropietario(uf.getTelefonoPropietario());

        dto.setNombreInquilino(uf.getNombreInquilino());
        dto.setApellidoInquilino(uf.getApellidoInquilino());
        dto.setMailInquilino(uf.getMailInquilino());
        dto.setTelefonoInquilino(uf.getTelefonoInquilino());

        EstadoCuentaUfDTO ecDto = getEstadoCuentaUfDTO(uf);
        dto.setEstadoCuentaUfDTO(ecDto);

        return dto;
    }

    private static EstadoCuentaUfDTO getEstadoCuentaUfDTO(UnidadFuncional uf) {
        EstadoCuentaUf ec = uf.getEstadoCuentaUf();
        EstadoCuentaUfDTO ecDto = new EstadoCuentaUfDTO();

        ecDto.setIdEstadoCuentaUf(ec.getIdEstadoCuentaUf());
        ecDto.setIdUf(uf.getIdUf());
        ecDto.setPeriodo(ec.getPeriodo());
        ecDto.setDeuda(ec.getDeuda());
        ecDto.setIntereses(ec.getIntereses());
        ecDto.setTotalA(ec.getTotalA());
        ecDto.setTotalB(ec.getTotalB());
        ecDto.setTotalB(ec.getTotalB());
        ecDto.setTotalC(ec.getTotalC());
        ecDto.setTotalD(ec.getTotalD());
        ecDto.setTotalE(ec.getTotalE());
        ecDto.setGastoParticular(ec.getGastoParticular());
        ecDto.setTotalExpensa(ec.getTotalExpensa());
        ecDto.setSaldoFinal(ec.getSaldoFinal());
        ecDto.setSaldoExpensa(ec.getSaldoExpensa());
        ecDto.setSaldoIntereses(ec.getSaldoIntereses());
        return ecDto;
    }

    private void actualizarPorcentajes(List<UnidadFuncional> ufs) {
        double valorInicial = 0;
        for (UnidadFuncional uf : ufs){
            if(uf.getPorcentajeUnidadB() == null){
                uf.setPorcentajeUnidadB(valorInicial);
            }
            if(uf.getPorcentajeUnidadC() == null){
                uf.setPorcentajeUnidadC(valorInicial);
            }
            if(uf.getPorcentajeUnidadB() == null){
                uf.setPorcentajeUnidadD(valorInicial);
            }
            if(uf.getPorcentajeUnidadB() == null){
                uf.setPorcentajeUnidadE(valorInicial);
            }
        }
    }

}
