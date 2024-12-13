package com.mad.sumerios.movimientos.egreso.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.enums.TipoEgreso;
import com.mad.sumerios.estadocuentaconsorcio.service.EstadoCuentaConsorcioService;
import com.mad.sumerios.expensa.repository.IExpensaRepository;
import com.mad.sumerios.movimientos.egreso.dto.EgresoCreateDTO;
import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.egreso.dto.EgresoUpdateDTO;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.egreso.repository.IEgresoRepository;
import com.mad.sumerios.proveedor.repository.IProveedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EgresoService {

    private final IEgresoRepository egresoRepository;
    private final IProveedorRepository proveedorRepository;
    private final IConsorcioRepository consorcioRepository;
    private final IExpensaRepository expensaRepository;
    private final EstadoCuentaConsorcioService estadoCuentaConsorcioService;

    @Autowired
    public EgresoService(IEgresoRepository egresoRepository,
                         IProveedorRepository proveedorRepository,
                         IConsorcioRepository consorcioRepository,
                         IExpensaRepository expensaRepository,
                         EstadoCuentaConsorcioService estadoCuentaConsorcioService) {
        this.egresoRepository = egresoRepository;
        this.proveedorRepository = proveedorRepository;
        this.consorcioRepository = consorcioRepository;
        this.expensaRepository = expensaRepository;
        this.estadoCuentaConsorcioService = estadoCuentaConsorcioService;
    }

    //  CREAR EGRESO
    @Transactional
    public void createEgreso(EgresoCreateDTO dto) throws Exception{
        Egreso egreso = mapToEgresoEntity(dto);
        if(egreso.getTipoEgreso() != TipoEgreso.FONDO_ADM) {
            Optional<Consorcio> consorcio = consorcioRepository.findById(egreso.getIdConsorcio());
            consorcio.ifPresent(value -> estadoCuentaConsorcioService.restarEgreso(value.getEstadoCuentaConsorcio(), egreso));
        }
        egresoRepository.save(egreso);
    }

    //  GETs
    // listar por consorcio y fecha
    public List<EgresoResponseDTO> getEgresosByIdConsorcioAndFecha(Long idConsorcio, LocalDate startDate, LocalDate endDate) {
        List<Egreso> egresos= egresoRepository.findByIdConsorcioAndFechaBetween(idConsorcio, startDate, endDate);
        return egresos.stream().map(this::mapToEgresoResponse).collect(Collectors.toList());
    }
    // listar por consorcio
    public List<EgresoResponseDTO> getEgresosByIdConsorcio(Long idConsorcio) {
        List<Egreso> egresos= egresoRepository.findByIdConsorcio(idConsorcio);
        return egresos.stream().map(this::mapToEgresoResponse).collect(Collectors.toList());
    }
    // listar por proveedor
    public List<EgresoResponseDTO> getEgresosByProveedor(Long idProveedor) {
        List<Egreso> egresos= egresoRepository.findByIdProveedor(idProveedor);
        return egresos.stream().map(this::mapToEgresoResponse).collect(Collectors.toList());
    }
    // listar por proveedor y consorcio
    public List<EgresoResponseDTO> getEgresosByProveedorAndConsorcio(Long idProveedor, Long idConsorcio) {
        List<Egreso> egresos= egresoRepository.findByIdProveedorAndIdConsorcio(idProveedor,idConsorcio);
        return egresos.stream().map(this::mapToEgresoResponse).collect(Collectors.toList());
    }
    // listar por Total final
    public List<EgresoResponseDTO> getEgresosByTotalFinal(Double totalFinal) {
        List<Egreso> egresos= egresoRepository.findByTotalFinal(totalFinal);
        return egresos.stream().map(this::mapToEgresoResponse).collect(Collectors.toList());
    }
    // buscar por comprobante
    public EgresoResponseDTO getEgresosByComprobante(String comprobante) {
        return mapToEgresoResponse(egresoRepository.findByComprobante(comprobante));
    }
    // buscar por expensa
//    public List<EgresoResponseDTO> getEgresosByExpensa(Long idConsorcio, YearMonth periodo) {
//        Expensa exp = expensaRepository.findByConsorcio_idConsorcioAndPeriodo(idConsorcio, periodo);
//        List<Egreso> egresos= egresoRepository.findByExpensa_IdExpensa(exp.getIdExpensa());
//        return egresos.stream().map(this::mapToEgresoResponse).collect(Collectors.toList());
//    }

    //  ACTUALIZAR CONSORCIO
    public void updateEgreso (Long idIngreso, EgresoUpdateDTO dto) throws Exception{
        Egreso egreso = egresoRepository.findById(idIngreso)
                .orElseThrow(()-> new Exception("Egreso no encontrado"));

        Egreso egresoUpdated = mapToEgresoEntityUpdate(dto);
        if(egreso.getTipoEgreso() != TipoEgreso.FONDO_ADM) {
            Optional<Consorcio> consorcio = consorcioRepository.findById(egreso.getIdConsorcio());
            consorcio.ifPresent(value -> estadoCuentaConsorcioService.modificarEgreso(value.getEstadoCuentaConsorcio(), egreso, egresoUpdated));

        }

        egreso.setIdConsorcio(egresoUpdated.getIdConsorcio());
        egreso.setIdProveedor(egreso.getIdProveedor());
        egreso.setFecha(egresoUpdated.getFecha());
        egreso.setTipoEgreso(egresoUpdated.getTipoEgreso());
        egreso.setFormaPago(egresoUpdated.getFormaPago());
        egreso.setTitulo(egresoUpdated.getTitulo());
        egreso.setDescripcion(egresoUpdated.getDescripcion());
        egreso.setComprobante(egresoUpdated.getComprobante());
        egreso.setTotalFinal(egresoUpdated.getTotalFinal());
        egreso.setCategoriaEgreso(egresoUpdated.getCategoriaEgreso());

        egresoRepository.save(egreso);
    }

    //  ELIMINAR EGRESO
    public void deleteEgreso(Long id) throws Exception{
        Egreso egreso = egresoRepository.findById(id)
                .orElseThrow(()-> new Exception("Egreso no encontrado"));
        if(egreso.getTipoEgreso() != TipoEgreso.FONDO_ADM) {
            Optional<Consorcio> consorcio = consorcioRepository.findById(egreso.getIdConsorcio());
            consorcio.ifPresent(value -> estadoCuentaConsorcioService.revertirEgreso(value.getEstadoCuentaConsorcio(), egreso));
        }
        egresoRepository.delete(egreso);
    }

    //  validaciones
    private void validateConsorcio(Long idConsorcio) throws Exception{
        if(consorcioRepository.findById(idConsorcio).isEmpty()){
            throw new Exception("Consorcio no encontrado");
        }
    }
    private void validateProveedor(Long idProveedor) throws Exception {
        if(proveedorRepository.findById(idProveedor).isEmpty()){
            throw new Exception("Proveedor no encontrado");
        }
    }
    private void validateComprobante(String comprobante) throws Exception {
        if(egresoRepository.findByComprobante(comprobante) != null){
            throw new Exception("El n° de comprobante ya existe");
        }
    }
    private void validateComprobanteUpdate(Long idEgreso, String comprobante) throws Exception {
        Egreso egresoExistente = egresoRepository.findByComprobante(comprobante);
        if (egresoExistente != null && !egresoExistente.getIdEgreso().equals(idEgreso)) {
            throw new Exception("El n° de comprobante ya existe para otro egreso.");
        }
    }

    private void validateValor(Double totalFinal) throws Exception {
        if (totalFinal <= 0){
            throw new Exception(
                    "El valor del egreso debe ser mayor de $0");
        }
    }
//    private Expensa validateExpensa(Long idExpensa) throws Exception {
//        Optional<Expensa> exp = expensaRepository.findById(idExpensa);
//        if(exp.isEmpty()){
//            throw new Exception("Expensa no encontrado");
//        }
//
//        return exp.get();
//    }

    //  mapeo DTO a Entity
    private Egreso mapToEgresoEntity(EgresoCreateDTO dto) throws Exception{
        validateConsorcio(dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        validateComprobante(dto.getComprobante());
        validateValor(dto.getTotalFinal());
//        Expensa exp = validateExpensa(dto.getIdExpensa());

        Egreso egreso = new Egreso();

//        egreso.setExpensa(exp);
        egreso.setIdConsorcio(dto.getIdConsorcio());
        egreso.setIdProveedor(dto.getIdProveedor());
        egreso.setFecha(dto.getFecha());
        egreso.setTitulo(dto.getTitulo());
        egreso.setTipoEgreso(dto.getTipoEgreso());
        egreso.setFormaPago(dto.getFormaPago());
        egreso.setComprobante(dto.getComprobante());
        egreso.setDescripcion(dto.getDescripcion());
        egreso.setTotalFinal(dto.getTotalFinal());
        egreso.setCategoriaEgreso(dto.getCategoriaEgreso());

        return egreso;
    }
    private Egreso mapToEgresoEntityUpdate(EgresoUpdateDTO dto) throws Exception{
        validateConsorcio(dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        validateComprobanteUpdate(dto.getIdEgreso(), dto.getComprobante());
        validateValor(dto.getTotalFinal());

        Egreso egreso = new Egreso();

        egreso.setIdConsorcio(dto.getIdConsorcio());
        egreso.setIdProveedor(dto.getIdProveedor());
        egreso.setFecha(dto.getFecha());
        egreso.setTitulo(dto.getTitulo());
        egreso.setTipoEgreso(dto.getTipoEgreso());
        egreso.setFormaPago(dto.getFormaPago());
        egreso.setComprobante(dto.getComprobante());
        egreso.setDescripcion(dto.getDescripcion());
        egreso.setTotalFinal(dto.getTotalFinal());
        egreso.setCategoriaEgreso(dto.getCategoriaEgreso());

        return egreso;
    }
    //  mapeo Entity a DTO
    private EgresoResponseDTO mapToEgresoResponse(Egreso egreso){
        EgresoResponseDTO dto = new EgresoResponseDTO();

        dto.setIdEgreso(egreso.getIdEgreso());
        dto.setIdConsorcio(egreso.getIdConsorcio());
        dto.setIdProveedor(egreso.getIdProveedor());
//        dto.setIdExpensa(egreso.getExpensa().getIdExpensa());
        dto.setFecha(egreso.getFecha());
        dto.setTitulo(egreso.getTitulo());
        dto.setFormaPago(egreso.getFormaPago());
        dto.setComprobante(egreso.getComprobante());
        dto.setDescripcion(egreso.getDescripcion());
        dto.setTotalFinal(egreso.getTotalFinal());
        dto.setTipoEgreso(egreso.getTipoEgreso());
        dto.setCategoriaEgreso(egreso.getCategoriaEgreso());

        return dto;
    }
}
