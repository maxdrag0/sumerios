package com.mad.sumerios.movimientos.pagouf.service;

import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFCreateDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFUpdateDTO;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.movimientos.pagouf.repository.IPagoUFRepository;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagoUFService {

    private final IPagoUFRepository pagoUFRepository;
    private final IUnidadFuncionalRepository ufRepository;
    private final IConsorcioRepository consorcioRepository;

    @Autowired
    public PagoUFService(IPagoUFRepository ingresoRepository,
                         IUnidadFuncionalRepository ufRepository,
                         IConsorcioRepository consorcioRepository) {
        this.pagoUFRepository  = ingresoRepository;
        this.ufRepository = ufRepository;
        this.consorcioRepository = consorcioRepository;
    }

    //  CREAR INGRESO
    public void createPagoUF (PagoUFCreateDTO dto) throws Exception{
        PagoUF pago = mapToPagoUFEntity(dto);
        validatePagoUF(pago);
        pagoUFRepository.save(pago);
    }

    //  LISTAR INGRESO
    //  por unidad funcional
    public List<PagoUFDTO> getPagoUFByUnidadFuncional(Long idUf){
        List<PagoUF> pagos = pagoUFRepository.findByUnidadFuncional_idUf(idUf);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por unidad funcional y fechas
    public List<PagoUFDTO> getPagosByUnidadFuncionalAndFecha(Long unidadFuncionalId, Date startDate, Date endDate) throws Exception {
        if(ufRepository.findById(unidadFuncionalId).isEmpty()){
            throw new Exception("Unidad Funcional no encontrada.");
        }

        List<PagoUF> pagos = pagoUFRepository.findByUnidadFuncional_idUfAndFechaBetween(unidadFuncionalId, startDate, endDate);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por consorcio
    public List<PagoUFDTO> getPagoUFByConsorcio(Long idConsorcio){
        List<PagoUF> pagos = pagoUFRepository.findByIdConsorcio(idConsorcio);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por consorcio y fechas
    public List<PagoUFDTO> getPagoUFByConsorcioAndFecha(Long idConsorcio, Date startDate, Date endDate){
        List<PagoUF> pagos = pagoUFRepository.findByIdConsorcioAndFechaBetween(idConsorcio, startDate, endDate);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  por consorcio y forma de pago
    public List<PagoUFDTO> getPagoUFByConsorcioAndFormaPago(Long idConsorcio, FormaPago formaPago){
        List<PagoUF> pagos = pagoUFRepository.findByIdConsorcioAndFormaPago(idConsorcio, formaPago);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }
    //  forma de pago
    public List<PagoUFDTO> getPagoUFByFormaPago(FormaPago formaPago){
        List<PagoUF> pagos = pagoUFRepository.findByFormaPago(formaPago);
        return pagos.stream().map(this::mapToPagoUFDTO).collect(Collectors.toList());
    }


    //  ACTUALIZAR INGRESO
    public void updatePagoUF(Long idPagoUf, PagoUFUpdateDTO dto) throws Exception{
        PagoUF pago = pagoUFRepository.findById(idPagoUf)
                .orElseThrow(()-> new Exception("Pago no encontrado"));
        PagoUF pagoUpdated = mapToPagoUFEntityUpdate(dto);
        validatePagoUF(pagoUpdated);

        pago.setFecha(pagoUpdated.getFecha());
        pago.setValor(pagoUpdated.getValor());
        pago.setDescripcion(pagoUpdated.getDescripcion());
        pago.setFormaPago(pagoUpdated.getFormaPago());

        pagoUFRepository.save(pago);
    }

    //  ELIMINAR INGRESO
    public void deletePagoUF(Long id) throws Exception{
        PagoUF pago = pagoUFRepository.findById(id)
                .orElseThrow(()-> new Exception("Pago UF no encontrado"));

        pagoUFRepository.delete(pago);
    }

    //validaciones
    private void validatePagoUF(PagoUF pago) throws Exception{
        if(pago == null){
            throw new Exception("Pago nulo");
        } else if (pago.getValor() < 0){
            throw new Exception("El pago no puede ser menor a 0");
        }
    }
    private void validateConsorcio(Long idConsorcio) throws Exception{
        if(consorcioRepository.findById(idConsorcio).isEmpty()) {
            throw new Exception("Consorcio no encontrado.");
        }
    }
    private UnidadFuncional validateUf(Long idUf) throws Exception{
        Optional<UnidadFuncional> uf = ufRepository.findById(idUf);
        if(uf.isEmpty()) {
            throw new Exception("Consorcio no encontrado.");
        }
        return uf.get();
    }
    private void validateValor(Double valor) throws Exception{
        if (valor <= 0){
            throw new Exception("El pago debe ser mayor a $0");
        }
    }
    // mapeo DTO a Entity
    private PagoUF mapToPagoUFEntity(PagoUFCreateDTO dto) throws Exception {
        UnidadFuncional uf = validateUf(dto.getIdUf());
        validateConsorcio(dto.getIdConsorcio());

        PagoUF pagoUF = new PagoUF();

        pagoUF.setUnidadFuncional(uf);
        pagoUF.setIdConsorcio(dto.getIdConsorcio());
        pagoUF.setFecha(dto.getFecha());
        pagoUF.setValor(dto.getValor());
        pagoUF.setFormaPago(dto.getFormaPago());
        pagoUF.setDescripcion(dto.getDescripcion());

        return pagoUF;
    }
    private PagoUF mapToPagoUFEntityUpdate(PagoUFUpdateDTO dto) throws Exception {
        validateValor(dto.getValor());

        PagoUF pagoUF = new PagoUF();

        pagoUF.setFecha(dto.getFecha());
        pagoUF.setValor(dto.getValor());
        pagoUF.setFormaPago(dto.getFormaPago());
        pagoUF.setDescripcion(dto.getDescripcion());

        return pagoUF;
    }

    // mapeo Entity a DTO
    private PagoUFDTO mapToPagoUFDTO(PagoUF pagoUF) {
        PagoUFDTO dto = new PagoUFDTO();

        dto.setIdPagoUF(pagoUF.getIdPagoUF());
        dto.setFecha(pagoUF.getFecha());
        dto.setValor(pagoUF.getValor());
        dto.setFormaPago(pagoUF.getFormaPago());
        dto.setDescripcion(pagoUF.getDescripcion());

        return dto;
    }

}
