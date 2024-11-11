package com.mad.sumerios.movimientos.ingreso.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.estadocuenta.service.EstadoCuentaService;
import com.mad.sumerios.expensa.model.Expensa;
import com.mad.sumerios.expensa.repository.IExpensaRepository;
import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoCreateDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoResponseDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoUpdateDTO;
import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
import com.mad.sumerios.movimientos.ingreso.repository.IIngresoRepository;
import com.mad.sumerios.proveedor.repository.IProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngresoService {

    private final IIngresoRepository ingresoRepository;
    private final IConsorcioRepository consorcioRepository;
    private final IProveedorRepository proveedorRepository;
    private final IExpensaRepository expensaRepository;
    private final EstadoCuentaService estadoCuentaService;

    @Autowired
    public IngresoService(IIngresoRepository ingresoRepository,
                          IConsorcioRepository consorcioRepository,
                          IProveedorRepository proveedorRepository,
                          IExpensaRepository expensaRepository,
                          EstadoCuentaService estadoCuentaService) {
        this.ingresoRepository  = ingresoRepository;
        this.consorcioRepository = consorcioRepository;
        this.proveedorRepository = proveedorRepository;
        this.expensaRepository = expensaRepository;
        this.estadoCuentaService = estadoCuentaService;
    }

    //  CREAR INGRESO
    public void createIngreso (IngresoCreateDTO dto) throws Exception{
        Ingreso ingreso = mapToIngresoEntity(dto);
        Optional<Consorcio> consorcio = consorcioRepository.findById(ingreso.getIdConsorcio());
        consorcio.ifPresent(value-> estadoCuentaService.sumarIngreso(value.getEstadoCuenta(), ingreso));
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
    // buscar por expensa
    public List<IngresoResponseDTO> getIngresoByExpensa(Long idConsorcio, YearMonth periodo) {
        Expensa exp = expensaRepository.findByConsorcio_idConsorcioAndPeriodo(idConsorcio, periodo);
        List<Ingreso> ingresos= ingresoRepository.findByExpensa_IdExpensa(exp.getIdExpensa());
        return ingresos.stream().map(this::mapToIngresoResponseDTO).collect(Collectors.toList());
    }

    //  ACTUALIZAR INGRESO
    public void updateIngreso(Long idIngreso, IngresoUpdateDTO dto) throws Exception{
        Ingreso ingreso = ingresoRepository.findById(idIngreso)
                .orElseThrow(()-> new Exception("Ingreso no encontrado"));
        Ingreso ingresoUpdated = mapToIngresoUpdate(dto);

        Optional<Consorcio> consorcio = consorcioRepository.findById(ingreso.getIdConsorcio());
        consorcio.ifPresent(value-> estadoCuentaService.modificarIngreso(value.getEstadoCuenta(), ingreso, ingresoUpdated));

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
        Optional<Consorcio> consorcio = consorcioRepository.findById(ingreso.getIdConsorcio());
        consorcio.ifPresent(value-> estadoCuentaService.revertirIngreso(value.getEstadoCuenta(), ingreso));
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
    private Expensa validateExpensa(Long idExpensa) throws Exception {
        Optional<Expensa> exp = expensaRepository.findById(idExpensa);
        if(exp.isEmpty()){
            throw new Exception("Expensa no encontrado");
        }

        return exp.get();
    }

    //  mapeo DTO a Entity
    private Ingreso mapToIngresoEntity(IngresoCreateDTO dto) throws Exception {
        validateNull(dto);
        validateConsorcio(dto.getIdConsorcio());
        validateValor(dto.getValor());
        validateProveedor(dto.getIdProveedor());
        Expensa exp = validateExpensa(dto.getIdExpensa());

        Ingreso ingreso = new Ingreso();

        ingreso.setExpensa(exp);
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
