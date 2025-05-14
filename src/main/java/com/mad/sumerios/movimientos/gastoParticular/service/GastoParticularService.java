package com.mad.sumerios.movimientos.gastoParticular.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.estadocuentaconsorcio.service.EstadoCuentaConsorcioService;
import com.mad.sumerios.expensa.model.Expensa;
import com.mad.sumerios.expensa.repository.IExpensaRepository;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularCreateDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularResponseDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularUpdateDTO;
import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import com.mad.sumerios.movimientos.gastoParticular.repository.IGastoParticularRepository;
import com.mad.sumerios.proveedor.repository.IProveedorRepository;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GastoParticularService {

    private final IGastoParticularRepository gastoParticularRepository;
    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final IProveedorRepository proveedorRepository;
    private final IExpensaRepository expensaRepository;
    private final EstadoCuentaConsorcioService estadoCuentaConsorcioService;
    private final IConsorcioRepository consorcioRepository;


    public GastoParticularService(IGastoParticularRepository gastoParticularRepository,
                                  IUnidadFuncionalRepository unidadFuncionalRepository,
                                  IProveedorRepository proveedorRepository,
                                  IExpensaRepository expensaRepository,
                                  EstadoCuentaConsorcioService estadoCuentaConsorcioService,
                                  IConsorcioRepository consorcioRepository){
        this.gastoParticularRepository = gastoParticularRepository;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.proveedorRepository = proveedorRepository;
        this.expensaRepository = expensaRepository;
        this.estadoCuentaConsorcioService = estadoCuentaConsorcioService;
        this.consorcioRepository = consorcioRepository;
    }

    // CRUD
    // CREATE GP.
    @Transactional
    public void createGastoParticular (GastoParticularCreateDTO dto) throws Exception{
        GastoParticular gastoParticular = mapToGastoParticularEntity(dto);

        if(gastoParticular.getPagoConsorcio()){
            Optional<Consorcio> consorcio = consorcioRepository.findById(dto.getIdConsorcio());
            consorcio.ifPresent(value-> estadoCuentaConsorcioService.restarGastoParticular(value.getEstadoCuentaConsorcio(), gastoParticular));
        }

        gastoParticularRepository.save(gastoParticular);
    }

//    // UPDATE Gp.
    public void updateGastoParticular (Long idGastoParticular, GastoParticularUpdateDTO dto) throws Exception{
        validateUfAndConsorcio(dto.getIdUf(), dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        validateTotalFinal(dto.getTotalFinal());
        justValidateExpensa(dto.getIdExpensa());

        GastoParticular gp = gastoParticularRepository.findById(idGastoParticular)
                .orElseThrow(()-> new Exception("Gasto Particular no encontrado."));

        Optional<Consorcio> consorcio = consorcioRepository.findById(dto.getIdConsorcio());

        if(gp.getPagoConsorcio()){
            consorcio.ifPresent(value-> estadoCuentaConsorcioService.modificarGastoParticular(value.getEstadoCuentaConsorcio(), gp, dto));
        } else if(dto.getPagoConsorcio()){
            consorcio.ifPresent(value-> estadoCuentaConsorcioService.restarGastoParticularUpdate(value.getEstadoCuentaConsorcio(), dto));
        }

        gp.setIdConsorcio(dto.getIdConsorcio());
        gp.setIdProveedor(dto.getIdProveedor());
        gp.setIdUf(dto.getIdUf());
        gp.setFecha(dto.getFecha());
        gp.setTitulo(dto.getTitulo());
        gp.setFormaPago(dto.getFormaPago());
        gp.setComprobante(dto.getComprobante());
        gp.setDescripcion(dto.getDescripcion());
        gp.setTotalFinal(dto.getTotalFinal());
        gp.setPagoConsorcio(dto.getPagoConsorcio());

        gastoParticularRepository.save(gp);
    }

    // DELETE Gp.
    public void deleteGastoParticular (Long idGastoParticular) throws Exception {
        GastoParticular gp = gastoParticularRepository.findById(idGastoParticular)
                .orElseThrow(()-> new Exception("Gasto Particular no encontrado"));

        if(gp.getPagoConsorcio()){
            Optional<Consorcio> consorcio = consorcioRepository.findById(gp.getIdConsorcio());
            consorcio.ifPresent(value-> estadoCuentaConsorcioService.revertirGastoParticular(value.getEstadoCuentaConsorcio(), gp));
        }

        gastoParticularRepository.delete(gp);
    }

    // GET Gp. por
    // proveedor
    public List<GastoParticularResponseDTO> findByProveedor (Long idProveedor) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdProveedor(idProveedor);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // consorcio
    public List<GastoParticularResponseDTO> findByConsorcio (Long idConsorcio) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdConsorcio(idConsorcio);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // consorcio y fechas
    public List<GastoParticularResponseDTO> findByConsorcioAndFecha (Long idConsorcio, LocalDate startDate, LocalDate endDate) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdConsorcioAndFechaBetween(idConsorcio,startDate,endDate );
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // consorcio y periodo
    public List<GastoParticularResponseDTO> findByPeriodoAndConsorcio (YearMonth periodo, Long idConsorcio) {
        List<GastoParticular> gastos = gastoParticularRepository.findByPeriodoAndIdConsorcio(periodo, idConsorcio);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // uf
    public List<GastoParticularResponseDTO> findByUf (Long idUf) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdUf(idUf);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // uf y fechas
    public List<GastoParticularResponseDTO> findByUfAndFecha (Long idUf, LocalDate startDate, LocalDate endDate) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdUfAndFechaBetween(idUf, startDate, endDate);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // total final
    public List<GastoParticularResponseDTO> findByTotalFinal (Double totalFinal) {
        List<GastoParticular> gastos = gastoParticularRepository.findByTotalFinal(totalFinal);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // buscar por expensa
    public List<GastoParticularResponseDTO> getGastoParticularByExpensa(Long idExpensa) {
        List<GastoParticular> gastos= gastoParticularRepository.findByExpensa_IdExpensa(idExpensa);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // buscar por comprobante
    public GastoParticularResponseDTO getGastoParticularByComprobante(String comprobante) {
        return mapToGastoParticularResponse(gastoParticularRepository.findByComprobante(comprobante));
    }

    // VALIDACIONES
    //  uf y consorcio`
    private void validateUfAndConsorcio(Long idUf, Long idConsorcio) throws Exception {
        UnidadFuncional uf = unidadFuncionalRepository.findById(idUf)
                .orElseThrow(()-> new Exception("Unidad Funcional no encontrada"));

        if(uf.getConsorcio().getIdConsorcio() != idConsorcio){
            throw new Exception("La unidad funcional no pertenece al consorcio proporcionado");
        }
    }
    //  proveedor
    private void validateProveedor(Long idProveedor) throws Exception {
        if(proveedorRepository.findById(idProveedor).isEmpty()){
            throw new Exception("Proveedor no encontrado.");
        }
    }


    //  total final
    private void validateTotalFinal(Double totalFinal) throws Exception {
        if(totalFinal<=0){
            throw new Exception(
                    "El valor del gasto debe ser mayor de $0");
        }
    }
    //  expensa
    private void justValidateExpensa(Long idExpensa) throws Exception {
        Optional<Expensa> exp = expensaRepository.findById(idExpensa);
        if(exp.isEmpty()){
            throw new Exception("Expensa no encontrado");
        }
    }

    private Expensa validateExpensa(Long idExpensa) throws Exception {
        Optional<Expensa> exp = expensaRepository.findById(idExpensa);
        if(exp.isEmpty()){
            throw new Exception("Expensa no encontrado");
        }

        return exp.get();
    }

    // MAP
    //  map DTO a Entity
    public GastoParticular mapToGastoParticularEntity(GastoParticularCreateDTO dto) throws Exception {
        validateUfAndConsorcio(dto.getIdUf(), dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        Expensa exp = validateExpensa(dto.getIdExpensa());
        validateTotalFinal(dto.getTotalFinal());

        GastoParticular gastoParticular = new GastoParticular();

        gastoParticular.setExpensa(exp);
        gastoParticular.setPeriodo(dto.getPeriodo());
        gastoParticular.setIdConsorcio(dto.getIdConsorcio());
        gastoParticular.setIdProveedor(dto.getIdProveedor());
        gastoParticular.setIdUf(dto.getIdUf());
        gastoParticular.setFecha(dto.getFecha());
        gastoParticular.setTitulo(dto.getTitulo());
        gastoParticular.setFormaPago(dto.getFormaPago());
        gastoParticular.setComprobante(dto.getComprobante());
        gastoParticular.setDescripcion(dto.getDescripcion());
        gastoParticular.setTotalFinal(dto.getTotalFinal());
        gastoParticular.setPagoConsorcio(dto.getPagoConsorcio());

        return gastoParticular;
    }

    //  map Entity a DTO
    public GastoParticularResponseDTO mapToGastoParticularResponse(GastoParticular gp) {
        GastoParticularResponseDTO dto = new GastoParticularResponseDTO();

        dto.setIdGastoParticular(gp.getIdGastoParticular());
        dto.setIdProveedor(gp.getIdProveedor());
        dto.setIdUf(gp.getIdUf());
        dto.setIdConsorcio(gp.getIdConsorcio());
        dto.setIdExpensa(gp.getExpensa().getIdExpensa());
        dto.setPeriodo(gp.getPeriodo());
        dto.setFecha(gp.getFecha());
        dto.setTitulo(gp.getTitulo());
        dto.setFormaPago(gp.getFormaPago());
        dto.setTipoEgreso(gp.getTIPO_EGRESO());
        dto.setComprobante(gp.getComprobante());
        dto.setDescripcion(gp.getDescripcion());
        dto.setPagoConsorcio(gp.getPagoConsorcio());
        dto.setTotalFinal(gp.getTotalFinal());

        return dto;
    }
}
