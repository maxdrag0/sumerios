package com.mad.sumerios.movimientos.gastoParticular.service;

import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularCreateDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularResponseDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularUpdateDTO;
import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import com.mad.sumerios.movimientos.gastoParticular.repository.IGastoParticularRepository;
import com.mad.sumerios.proveedor.repository.IProveedorRepository;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GastoParticularService {

    private final IGastoParticularRepository gastoParticularRepository;
    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final IProveedorRepository proveedorRepository;

    public GastoParticularService(IGastoParticularRepository gastoParticularRepository,
                                  IUnidadFuncionalRepository unidadFuncionalRepository,
                                  IProveedorRepository proveedorRepository){
        this.gastoParticularRepository = gastoParticularRepository;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.proveedorRepository = proveedorRepository;
    }

    // CRUD
    // CREATE GP.
    public void createGastoParticular (GastoParticularCreateDTO dto) throws Exception{
        GastoParticular gastoParticular = mapToGastoParticularEntity(dto);
        gastoParticularRepository.save(gastoParticular);
    }
    // UPDATE Gp.
    public void updateGastoParticular (Long idGastoParticular, GastoParticularUpdateDTO dto) throws Exception{
        GastoParticular gp = gastoParticularRepository.findById(idGastoParticular)
                .orElseThrow(()-> new Exception("Gasto Particular no encontrado."));

        GastoParticular gpUpdated = mapToGastoParticularEntityUpdate(dto);

        //        gp.setIdExpensa(dto.getIdExpensa);
        gp.setIdConsorcio(gpUpdated.getIdConsorcio());
        gp.setIdProveedor(gpUpdated.getIdProveedor());
        gp.setIdUf(gpUpdated.getIdUf());
        gp.setFecha(gpUpdated.getFecha());
        gp.setTitulo(gpUpdated.getTitulo());
        gp.setFormaPago(gpUpdated.getFormaPago());
        gp.setComprobante(gpUpdated.getComprobante());
        gp.setDescripcion(gpUpdated.getDescripcion());
        gp.setTotalFinal(gpUpdated.getTotalFinal());
    }
    // DELETE Gp.
    public void deleteGastoParticular (Long idGastoParticular) throws Exception {
        GastoParticular gp = gastoParticularRepository.findById(idGastoParticular)
                .orElseThrow(()-> new Exception("Gasto Particular no encontrado"));

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
    public List<GastoParticularResponseDTO> findByConsorcioAndFecha (Long idConsorcio, Date startDate, Date endDate) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdConsorcioAndFechaBetween(idConsorcio,startDate,endDate );
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // uf
    public List<GastoParticularResponseDTO> findByUf (Long idUf) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdUf(idUf);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // uf y fechas
    public List<GastoParticularResponseDTO> findByUfAndFecha (Long idUf, Date startDate, Date endDate) {
        List<GastoParticular> gastos = gastoParticularRepository.findByIdUfAndFechaBetween(idUf, startDate, endDate);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
    }
    // total final
    public List<GastoParticularResponseDTO> findByTotalFinal (Double totalFinal) {
        List<GastoParticular> gastos = gastoParticularRepository.findByTotalFinal(totalFinal);
        return gastos.stream().map(this::mapToGastoParticularResponse).collect(Collectors.toList());
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
    //  comprobante
    private void validateComprobante(String comprobante) throws Exception {
        if(gastoParticularRepository.findByComprobante(comprobante) != null){
            throw new Exception("El n° de comprobante ya existe");
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
//    private void validateExpensa(Long idExpensa) throws Exception {
//        if(expensaRepository.findById(dto.getIdExpensa()).isEmpty()){
//            throw new Exception("Expensa no encontrado");
//        }
//    }

    // MAP
    //  map DTO a Entity
    public GastoParticular mapToGastoParticularEntity(GastoParticularCreateDTO dto) throws Exception {
        validateUfAndConsorcio(dto.getIdUf(), dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        validateComprobante(dto.getComprobante());
//        validateExpensa(dto.getIdExpensa);
        validateTotalFinal(dto.getTotalFinal());

        GastoParticular gastoParticular = new GastoParticular();

//        gastoParticular.setIdExpensa(dto.getIdExpensa);
        gastoParticular.setIdConsorcio(dto.getIdConsorcio());
        gastoParticular.setIdProveedor(dto.getIdProveedor());
        gastoParticular.setIdUf(dto.getIdUf());
        gastoParticular.setFecha(dto.getFecha());
        gastoParticular.setTitulo(dto.getTitulo());
        gastoParticular.setFormaPago(dto.getFormaPago());
        gastoParticular.setComprobante(dto.getComprobante());
        gastoParticular.setDescripcion(dto.getDescripcion());
        gastoParticular.setTotalFinal(dto.getTotalFinal());

        return gastoParticular;
    }
    public GastoParticular mapToGastoParticularEntityUpdate(GastoParticularUpdateDTO dto) throws Exception{
        validateUfAndConsorcio(dto.getIdUf(), dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        validateComprobante(dto.getComprobante());
//        validateExpensa(dto.getIdExpensa);
        validateTotalFinal(dto.getTotalFinal());

        GastoParticular gastoParticular = new GastoParticular();

//        gastoParticular.setIdExpensa(dto.getIdExpensa);
        gastoParticular.setIdConsorcio(dto.getIdConsorcio());
        gastoParticular.setIdProveedor(dto.getIdProveedor());
        gastoParticular.setIdUf(dto.getIdUf());
        gastoParticular.setFecha(dto.getFecha());
        gastoParticular.setTitulo(dto.getTitulo());
        gastoParticular.setFormaPago(dto.getFormaPago());
        gastoParticular.setComprobante(dto.getComprobante());
        gastoParticular.setDescripcion(dto.getDescripcion());
        gastoParticular.setTotalFinal(dto.getTotalFinal());

        return gastoParticular;
    }
    //  map Entity a DTO
    public GastoParticularResponseDTO mapToGastoParticularResponse(GastoParticular gp) {
        GastoParticularResponseDTO dto = new GastoParticularResponseDTO();

        dto.setIdGastoParticular(gp.getIdGastoParticular());
        dto.setIdProveedor(gp.getIdProveedor());
        dto.setIdUf(gp.getIdUf());
        dto.setIdConsorcio(gp.getIdConsorcio());
        dto.setFecha(gp.getFecha());
        dto.setTitulo(gp.getTitulo());
        dto.setFormaPago(gp.getFormaPago());
        dto.setComprobante(gp.getComprobante());
        dto.setDescripcion(gp.getDescripcion());
        dto.setTotalFinal(gp.getTotalFinal());

        return dto;
    }
}
