package com.mad.sumerios.movimientos.egreso.service;

import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.movimientos.egreso.dto.EgresoCreateDTO;
import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.egreso.dto.EgresoUpdateDTO;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.egreso.repository.IEgresoRepository;
import com.mad.sumerios.proveedor.repository.IProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EgresoService {

    private final IEgresoRepository egresoRepository;
    private final IProveedorRepository proveedorRepository;
    private final IConsorcioRepository consorcioRepository;

    @Autowired
    public EgresoService(IEgresoRepository egresoRepository,
                         IProveedorRepository proveedorRepository,
                         IConsorcioRepository consorcioRepository) {
        this.egresoRepository = egresoRepository;
        this.proveedorRepository = proveedorRepository;
        this.consorcioRepository = consorcioRepository;
    }

    //  CREAR EGRESO
    public void createEgreso(EgresoCreateDTO dto) throws Exception{
        Egreso egreso = mapToEgresoEntity(dto);
        egresoRepository.save(egreso);
    }

    //  GETs
    // listar por consorcio y fecha
    public List<EgresoResponseDTO> getEgresosByIdConsorcioAndFecha(Long idConsorcio, Date startDate, Date endDate) {
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

    //  ACTUALIZAR CONSORCIO
    public void updateEgreso (Long idIngreso, EgresoUpdateDTO dto) throws Exception{
        Egreso egreso = egresoRepository.findById(idIngreso)
                .orElseThrow(()-> new Exception("Egreso no encontrado"));

        Egreso egresoUpdated = mapToEgresoEntityUpdate(dto);

        egreso.setIdConsorcio(egresoUpdated.getIdConsorcio());
        egreso.setIdProveedor(egreso.getIdProveedor());
        egreso.setFecha(egresoUpdated.getFecha());
        egreso.setTipoEgreso(egresoUpdated.getTipoEgreso());
        egreso.setTitulo(egresoUpdated.getTitulo());
        egreso.setDescripcion(egresoUpdated.getDescripcion());
        egreso.setComprobante(egresoUpdated.getComprobante());
        egreso.setTotalFinal(egresoUpdated.getTotalFinal());
        egreso.setTotalA(egresoUpdated.getTotalA());
        egreso.setTotalB(egresoUpdated.getTotalB());
        egreso.setTotalC(egresoUpdated.getTotalC());
        egreso.setTotalD(egresoUpdated.getTotalD());
        egreso.setTotalE(egresoUpdated.getTotalE());

        egresoRepository.save(egreso);
    }

    //  ELIMINAR EGRESO
    public void deleteEgreso(Long id) throws Exception{
        Egreso egreso = egresoRepository.findById(id)
                .orElseThrow(()-> new Exception("Egreso no encontrado"));

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
    private void validateValor(Double totalFinal, Double totalA, Double totalB, Double totalC, Double totalD, Double totalE) throws Exception {
        double suma = totalA+totalB+totalC+totalD+totalE;
        if(suma != totalFinal) {
            throw new Exception(
                    "El reparto valores es de $"+ suma +" mientras que el total es de $"+ totalFinal);
        } else if (suma >= 0){
            throw new Exception(
                    "El valor del egreso debe ser mayor de $0");
        }
    }
//    private void validateExpensa(Long idExpensa) throws Exception {
//        if(expensaRepository.findById(dto.getIdExpensa()).isEmpty()){
//            throw new Exception("Expensa no encontrado");
//        }
//    }

    //  mapeo DTO a Entity
    private Egreso mapToEgresoEntity(EgresoCreateDTO dto) throws Exception{
        validateConsorcio(dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        validateComprobante(dto.getComprobante());
        validateValor(dto.getTotalFinal(),
                      dto.getTotalA(),
                      dto.getTotalB(),
                      dto.getTotalC(),
                      dto.getTotalD(),
                      dto.getTotalE());
//        validateExpensa(dto.getIdExpensa);

        Egreso egreso = new Egreso();

//        egreso.setExpensa(expensaOptional.get());
        egreso.setIdConsorcio(dto.getIdConsorcio());
        egreso.setIdProveedor(dto.getIdProveedor());
        egreso.setFecha(dto.getFecha());
        egreso.setTitulo(dto.getTitulo());
        egreso.setTipoEgreso(dto.getTipoEgreso());
        egreso.setFormaPago(dto.getFormaPago());
        egreso.setComprobante(dto.getComprobante());
        egreso.setDescripcion(dto.getDescripcion());
        egreso.setTotalFinal(dto.getTotalFinal());
        egreso.setTotalA(dto.getTotalA());
        egreso.setTotalA(dto.getTotalB());
        egreso.setTotalA(dto.getTotalC());
        egreso.setTotalA(dto.getTotalD());
        egreso.setTotalA(dto.getTotalE());

        return egreso;
    }
    private Egreso mapToEgresoEntityUpdate(EgresoUpdateDTO dto) throws Exception{
        validateConsorcio(dto.getIdConsorcio());
        validateProveedor(dto.getIdProveedor());
        validateComprobante(dto.getComprobante());
        validateValor(dto.getTotalFinal(),
                dto.getTotalA(),
                dto.getTotalB(),
                dto.getTotalC(),
                dto.getTotalD(),
                dto.getTotalE());
//        validateExpensa(dto.getIdExpensa);

        Egreso egreso = new Egreso();

//        egreso.setExpensa(expensaOptional.get());
        egreso.setIdConsorcio(dto.getIdConsorcio());
        egreso.setIdProveedor(dto.getIdProveedor());
        egreso.setFecha(dto.getFecha());
        egreso.setTitulo(dto.getTitulo());
        egreso.setTipoEgreso(dto.getTipoEgreso());
        egreso.setFormaPago(dto.getFormaPago());
        egreso.setComprobante(dto.getComprobante());
        egreso.setDescripcion(dto.getDescripcion());
        egreso.setTotalFinal(dto.getTotalFinal());
        egreso.setTotalA(dto.getTotalA());
        egreso.setTotalA(dto.getTotalB());
        egreso.setTotalA(dto.getTotalC());
        egreso.setTotalA(dto.getTotalD());
        egreso.setTotalA(dto.getTotalE());

        return egreso;
    }
    //  mapeo Entity a DTO
    private EgresoResponseDTO mapToEgresoResponse(Egreso egreso){
        EgresoResponseDTO dto = new EgresoResponseDTO();

        dto.setIdEgreso(egreso.getIdEgreso());
        dto.setIdConsorcio(egreso.getIdConsorcio());
        dto.setIdProveedor(egreso.getIdProveedor());
        dto.setFecha(egreso.getFecha());
        dto.setTitulo(egreso.getTitulo());
        dto.setFormaPago(egreso.getFormaPago());
        dto.setComprobante(egreso.getComprobante());
        dto.setDescripcion(egreso.getDescripcion());
        dto.setTotalFinal(egreso.getTotalFinal());

        return dto;
    }
}
