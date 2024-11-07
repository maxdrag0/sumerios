package com.mad.sumerios.movimientos.ingreso.service;

import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoCreateDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoResponseDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoUpdateDTO;
import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
import com.mad.sumerios.movimientos.ingreso.repository.IIngresoRepository;
import com.mad.sumerios.proveedor.repository.IProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngresoService {

    private final IIngresoRepository ingresoRepository;
    private final IConsorcioRepository consorcioRepository;
    private final IProveedorRepository proveedorRepository;
//    private final IExpensaRepository expensaRepository;

    @Autowired
    public IngresoService(IIngresoRepository ingresoRepository,
                          IConsorcioRepository consorcioRepository,
                          IProveedorRepository proveedorRepository
                          ) {
        this.ingresoRepository  = ingresoRepository;
        this.consorcioRepository = consorcioRepository;
        this.proveedorRepository = proveedorRepository;
//        this.expensaRepository = expensaRepository;
    }

    //  CREAR INGRESO
    public void createIngreso (IngresoCreateDTO dto) throws Exception{
        Ingreso ingreso = mapToIngresoEntity(dto);
        ingresoRepository.save(ingreso);
    }

    //  LISTAR INGRESO
    //  por consorcio
    public List<IngresoResponseDTO> getIngresoByConsorcio(Long idConsorcio){
        List<Ingreso> ingresos = ingresoRepository.findByIdConsorcio(idConsorcio);
        return ingresos.stream().map(this::mapToIngresoResponseDTO).collect(Collectors.toList());
    }
    //  por consorcio y fechas
    public List<IngresoResponseDTO> getIngresoByConsorcioYFecha(Long idConsorcio, Date startDate, Date endDate){
        List<Ingreso> ingresos = ingresoRepository.findByIdConsorcioAndFechaBetween(idConsorcio, startDate, endDate);
        return ingresos.stream().map(this::mapToIngresoResponseDTO).collect(Collectors.toList());
    }
    //  por proveedor
    public List<IngresoResponseDTO> getIngresoByProveedor(Long idProveedor){
        List<Ingreso> ingresos = ingresoRepository.findByIdProveedor(idProveedor);
        return ingresos.stream().map(this::mapToIngresoResponseDTO).collect(Collectors.toList());
    }
    //  por proveedor y consorcio
    public List<IngresoResponseDTO> getIngresoByProveedorAndConsorcio(Long idProveedor,Long idConsorcio){
        List<Ingreso> ingresos = ingresoRepository.findByIdProveedorAndIdConsorcio(idProveedor, idConsorcio);
        return ingresos.stream().map(this::mapToIngresoResponseDTO).collect(Collectors.toList());
    }

    //  ACTUALIZAR INGRESO
    public void updateIngreso(Long idIngreso, IngresoUpdateDTO dto) throws Exception{
        Ingreso ingreso = ingresoRepository.findById(idIngreso)
                .orElseThrow(()-> new Exception("Ingreso no encontrado"));
        Ingreso ingresoUpdated = mapToIngresoUpdate(dto);

        ingreso.setFecha(ingresoUpdated.getFecha());
        ingreso.setValor(ingresoUpdated.getValor());
        ingreso.setTitulo(ingresoUpdated.getTitulo());
        ingreso.setDescripcion(ingresoUpdated.getDescripcion());
        ingreso.setFormaPago(ingresoUpdated.getFormaPago());

        ingresoRepository.save(ingreso);
    }

    //  ELIMINAR INGRESO
    public void deleteIngreso(Long id) throws Exception{
        Ingreso ingreso = ingresoRepository.findById(id)
                .orElseThrow(()-> new Exception("Ingreso no encontrado"));

        ingresoRepository.delete(ingreso);
    }

    //  validaciones
    private void validateNull(Object object) throws Exception {
        if(object == null){
            throw new Exception("El objeto ingresado es nulo");
        }
    }
    private void validateProveedor(Long idProveedor)throws Exception {
        if(proveedorRepository.findById(idProveedor).isEmpty()){
            throw new Exception("Proveedor no encontrado");
        }
    }
    private void validateConsorcio(Long idConsorcio) throws Exception{
        if(consorcioRepository.findById(idConsorcio).isEmpty()){
            throw new Exception("Consorcio no encontrado");
        }
    }
    private void validateValor(Double valor) throws Exception {
        if(valor <= 0 ) {
            throw new Exception("El ingreso debe tener un valor mayor a $0");
        }
    }
    //    private void validateExpensa(Long idExpensa) throws Exception {
    //        if(expensaRepository.findById(dto.getIdExpensa()).isEmpty()){
    //            throw new Exception("Expensa no encontrado");
    //        }
    //    }

    //  mapeo DTO a Entity
    private Ingreso mapToIngresoEntity(IngresoCreateDTO dto) throws Exception {
        validateNull(dto);
        validateConsorcio(dto.getIdConsorcio());
        validateValor(dto.getValor());
        validateProveedor(dto.getIdProveedor());
//        validateExpensa(dto.getIdExpensa);

        Ingreso ingreso = new Ingreso();

//        ingreso.setExpensa(expensaOptional.get());
        ingreso.setIdProveedor(dto.getIdProveedor());
        ingreso.setIdConsorcio(dto.getIdConsorcio());
        ingreso.setFecha(dto.getFecha());
        ingreso.setValor(dto.getValor());
        ingreso.setTitulo(dto.getTitulo());
        ingreso.setDescripcion(dto.getDescripcion());
        ingreso.setFormaPago(dto.getFormaPago());


        return ingreso;
    }
    private Ingreso mapToIngresoUpdate(IngresoUpdateDTO dto) throws Exception {
        validateNull(dto);
        validateValor(dto.getValor());
        validateProveedor(dto.getIdProveedor());
        Ingreso ingreso = new Ingreso();

        if(ingresoRepository.findById(dto.getIdIngreso()).isEmpty()){
            throw new Exception("Ingreso con ID: "+dto.getIdIngreso()+" no encontrado.");
        }

        ingreso.setIdProveedor(dto.getIdProveedor());
        ingreso.setFecha(dto.getFecha());
        ingreso.setValor(dto.getValor());
        ingreso.setTitulo(dto.getTitulo());
        ingreso.setDescripcion(dto.getDescripcion());
        ingreso.setFormaPago(dto.getFormaPago());

        return ingreso;
    }
    //  mapeo Entity a DTO
    private IngresoResponseDTO mapToIngresoResponseDTO (Ingreso ingreso) {
        IngresoResponseDTO dto = new IngresoResponseDTO();

        dto.setIdIngreso(ingreso.getIdIngreso());
        dto.setIdProveedor(ingreso.getIdProveedor());
        dto.setFecha(ingreso.getFecha());
        dto.setValor(ingreso.getValor());
        dto.setTitulo(ingreso.getTitulo());
        dto.setDescripcion(ingreso.getDescripcion());
        dto.setFormaPago(ingreso.getFormaPago());

        return dto;
    }
}
