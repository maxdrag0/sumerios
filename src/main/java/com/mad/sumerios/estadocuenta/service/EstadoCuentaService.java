package com.mad.sumerios.estadocuenta.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.enums.TipoEgreso;
import com.mad.sumerios.estadocuenta.dto.EstadoCuentaCreateDTO;
import com.mad.sumerios.estadocuenta.dto.EstadoCuentaDTO;
import com.mad.sumerios.estadocuenta.model.EstadoCuenta;
import com.mad.sumerios.estadocuenta.repository.IEstadoCuentaRepository;
import com.mad.sumerios.movimientos.egreso.model.Egreso;
import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstadoCuentaService {

    private final IEstadoCuentaRepository estadoCuentaRepository;
    private final IConsorcioRepository consorcioRepository;

    @Autowired
    public EstadoCuentaService (IEstadoCuentaRepository estadoCuentaRepository,
                                IConsorcioRepository consorcioRepository){
        this.estadoCuentaRepository = estadoCuentaRepository;
        this.consorcioRepository = consorcioRepository;
    }

    //  CREATE ESTADO CUENTA
    public void createEstadoCuenta (EstadoCuentaCreateDTO dto) throws Exception{
        estadoCuentaRepository.save(mapToEstadoCuentaEntity(dto));
    }

    //  UPDATE ESTADO CUENTA
    public void updateEstadoCuenta (Long idEstadoCuenta, EstadoCuentaDTO dto) throws Exception {
        EstadoCuenta ea = estadoCuentaRepository.findById(idEstadoCuenta)
                .orElseThrow(()-> new Exception("Estado de cuenta no encontrado con el ID: " + idEstadoCuenta));

        EstadoCuenta eaUpdated = mapToEstadoCuentaEntityUpdate(dto);

        ea.setEfectivo(dto.getEfectivo());
        ea.setBanco(dto.getBanco());
        ea.setFondoAdm(dto.getFondoAdm());
        ea.setTotal(dto.getTotal());

        estadoCuentaRepository.save(ea);
    }

    //  GET
    //  all
    public List<EstadoCuentaDTO> getAllEstadoCuenta (){
        List<EstadoCuenta> eas = estadoCuentaRepository.findAll();
        return eas.stream().map(this::mapToEstadoCuentaDTO).collect(Collectors.toList());
    }
    //  by consorcio
    public EstadoCuentaDTO getByIdConsorcio (Long idConsorcio){
        EstadoCuenta ea = estadoCuentaRepository.findByConsorcio_idConsorcio(idConsorcio);
        return mapToEstadoCuentaDTO(ea);
    }


    // MODIFICACIONES
    // Egreso
    // Restar
    public void restarEgreso(EstadoCuenta estadoCuenta, Egreso egreso){
        estadoCuenta.setTotal(estadoCuenta.getTotal() - egreso.getTotalFinal());
        if(egreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - egreso.getTotalFinal());
        } else {
            estadoCuenta.setBanco(estadoCuenta.getBanco() - egreso.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Revertir
    public void revertirEgreso(EstadoCuenta estadoCuenta, Egreso egreso){
        estadoCuenta.setTotal(estadoCuenta.getTotal() + egreso.getTotalFinal());
        if(egreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + egreso.getTotalFinal());
        } else {
            estadoCuenta.setBanco(estadoCuenta.getBanco() + egreso.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Modificar
    public void modificarEgreso(EstadoCuenta estadoCuenta, Egreso egresoViejo, Egreso egresoNuevo){
        if(egresoViejo.getFormaPago() == egresoNuevo.getFormaPago()){
            updateMismaFormaPagoEgreso(estadoCuenta, egresoViejo, egresoNuevo);
        } else{
            updateDistintaFormaPagoEgreso(estadoCuenta, egresoViejo, egresoNuevo);
        }
    }
    private void updateDistintaFormaPagoEgreso(EstadoCuenta estadoCuenta, Egreso egresoViejo, Egreso egresoNuevo) {
        estadoCuenta.setTotal(estadoCuenta.getTotal() + egresoViejo.getTotalFinal());
        estadoCuenta.setTotal(estadoCuenta.getTotal() - egresoNuevo.getTotalFinal());

        if(egresoViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + egresoViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuenta.setBanco(estadoCuenta.getBanco() - egresoNuevo.getTotalFinal());
        } else{
            // reversion gasto original
            estadoCuenta.setBanco(estadoCuenta.getBanco() + egresoViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - egresoNuevo.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    private void updateMismaFormaPagoEgreso(EstadoCuenta estadoCuenta, Egreso egresoViejo, Egreso egresoNuevo) {
        Double diferencia = egresoNuevo.getTotalFinal() - egresoViejo.getTotalFinal();
        estadoCuenta.setTotal(estadoCuenta.getTotal() + diferencia);
        if(egresoViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + diferencia);
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuenta);
    }

    // Gasto particular
    // Restar
    public void restarGastoParticular(EstadoCuenta estadoCuenta, GastoParticular gastoParticular){
        estadoCuenta.setTotal(estadoCuenta.getTotal() - gastoParticular.getTotalFinal());
        if(gastoParticular.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - gastoParticular.getTotalFinal());
        } else {
            estadoCuenta.setBanco(estadoCuenta.getBanco() - gastoParticular.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Revertir
    public void revertirGastoParticular(EstadoCuenta estadoCuenta, GastoParticular gastoParticular){
        estadoCuenta.setTotal(estadoCuenta.getTotal() + gastoParticular.getTotalFinal());
        if(gastoParticular.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + gastoParticular.getTotalFinal());
        } else {
            estadoCuenta.setBanco(estadoCuenta.getBanco() + gastoParticular.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Modificar
    public void modificarGastoParticular(EstadoCuenta estadoCuenta, GastoParticular gastoParticularViejo, GastoParticular gastoParticularNuevo){
        if(gastoParticularViejo.getFormaPago() == gastoParticularNuevo.getFormaPago()){
            updateMismaFormaPagoGastoParticular(estadoCuenta, gastoParticularViejo, gastoParticularNuevo);
        } else{
            updateDistintaFormaPagoGastoParticular(estadoCuenta, gastoParticularViejo, gastoParticularNuevo);
        }
    }
    private void updateDistintaFormaPagoGastoParticular(EstadoCuenta estadoCuenta, GastoParticular gastoParticularViejo, GastoParticular gastoParticularNuevo) {
        estadoCuenta.setTotal(estadoCuenta.getTotal() + gastoParticularViejo.getTotalFinal());
        estadoCuenta.setTotal(estadoCuenta.getTotal() - gastoParticularNuevo.getTotalFinal());

        if(gastoParticularViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + gastoParticularViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuenta.setBanco(estadoCuenta.getBanco() - gastoParticularNuevo.getTotalFinal());
        } else{
            // reversion gasto original
            estadoCuenta.setBanco(estadoCuenta.getBanco() + gastoParticularViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - gastoParticularNuevo.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    private void updateMismaFormaPagoGastoParticular(EstadoCuenta estadoCuenta, GastoParticular gastoParticularViejo, GastoParticular gastoParticularNuevo) {
        Double diferencia = gastoParticularNuevo.getTotalFinal() - gastoParticularViejo.getTotalFinal();
        estadoCuenta.setTotal(estadoCuenta.getTotal() + diferencia);
        if(gastoParticularViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + diferencia);
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuenta);
    }


    // Ingreso
    // Sumar
    public void sumarIngreso(EstadoCuenta estadoCuenta, Ingreso ingreso){
        estadoCuenta.setTotal(estadoCuenta.getTotal() + ingreso.getValor());
        if(ingreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + ingreso.getValor());
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() + ingreso.getValor());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Revertir
    public void revertirIngreso(EstadoCuenta estadoCuenta, Ingreso ingreso){
        estadoCuenta.setTotal(estadoCuenta.getTotal() - ingreso.getValor());
        if(ingreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - ingreso.getValor());
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() - ingreso.getValor());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Modificar
    public void modificarIngreso(EstadoCuenta estadoCuenta, Ingreso ingresoViejo, Ingreso ingresoNuevo){
        if(ingresoViejo.getFormaPago() == ingresoNuevo.getFormaPago()){
            updateMismaFormaPagoIngreso(estadoCuenta, ingresoViejo, ingresoNuevo);
        } else{
            updateDistintaFormaPagoIngreso(estadoCuenta, ingresoViejo, ingresoNuevo);
        }
    }
    private void updateDistintaFormaPagoIngreso(EstadoCuenta estadoCuenta, Ingreso ingresoViejo, Ingreso ingresoNuevo) {
        estadoCuenta.setTotal(estadoCuenta.getTotal() - ingresoViejo.getValor());
        estadoCuenta.setTotal(estadoCuenta.getTotal() + ingresoNuevo.getValor());

        if(ingresoViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - ingresoViejo.getValor());
            // descuento gasto nuevo
            estadoCuenta.setBanco(estadoCuenta.getBanco() + ingresoNuevo.getValor());
        } else{
            // reversion gasto original
            estadoCuenta.setBanco(estadoCuenta.getBanco() - ingresoViejo.getValor());
            // descuento gasto nuevo
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + ingresoNuevo.getValor());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    private void updateMismaFormaPagoIngreso(EstadoCuenta estadoCuenta, Ingreso ingresoViejo, Ingreso ingresoNuevo) {
        Double diferencia = ingresoNuevo.getValor() - ingresoViejo.getValor();
        estadoCuenta.setTotal(estadoCuenta.getTotal() + diferencia);
        if(ingresoViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + diferencia);
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuenta);
    }


    // PagoUF
    // Sumar
    public void sumarPagoUF(EstadoCuenta estadoCuenta, PagoUF pago){
        estadoCuenta.setTotal(estadoCuenta.getTotal() + pago.getValor());
        if(pago.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + pago.getValor());
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() + pago.getValor());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Revertir
    public void revertirPagoUF(EstadoCuenta estadoCuenta, PagoUF pago){
        estadoCuenta.setTotal(estadoCuenta.getTotal() - pago.getValor());
        if(pago.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - pago.getValor());
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() - pago.getValor());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    // Modificar
    public void modificarPagoUF(EstadoCuenta estadoCuenta, PagoUF pagoViejo, PagoUF pagoNuevo){
        if(pagoViejo.getFormaPago() == pagoNuevo.getFormaPago()){
            updateMismaFormaPagoPagoUF(estadoCuenta, pagoViejo, pagoNuevo);
        } else{
            updateDistintaPagoUF(estadoCuenta, pagoViejo, pagoNuevo);
        }
    }
    private void updateDistintaPagoUF(EstadoCuenta estadoCuenta, PagoUF pagoViejo, PagoUF pagoNuevo) {
        estadoCuenta.setTotal(estadoCuenta.getTotal() - pagoViejo.getValor());
        estadoCuenta.setTotal(estadoCuenta.getTotal() + pagoNuevo.getValor());

        if(pagoViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() - pagoViejo.getValor());
            // descuento gasto nuevo
            estadoCuenta.setBanco(estadoCuenta.getBanco() + pagoNuevo.getValor());
        } else{
            // reversion gasto original
            estadoCuenta.setBanco(estadoCuenta.getBanco() - pagoViejo.getValor());
            // descuento gasto nuevo
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + pagoNuevo.getValor());
        }

        estadoCuentaRepository.save(estadoCuenta);
    }
    private void updateMismaFormaPagoPagoUF(EstadoCuenta estadoCuenta, PagoUF pagoViejo, PagoUF pagoNuevo) {
        Double diferencia = pagoNuevo.getValor() - pagoViejo.getValor();
        estadoCuenta.setTotal(estadoCuenta.getTotal() + diferencia);
        if(pagoViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuenta.setEfectivo(estadoCuenta.getEfectivo() + diferencia);
        } else{
            estadoCuenta.setBanco(estadoCuenta.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuenta);
    }


    // VALIDACIONES
    private Consorcio validateConsorcio(Long idConsorcio) throws Exception {
        Optional<Consorcio> consorcio = consorcioRepository.findById(idConsorcio);
        if(consorcio.isEmpty()){
            throw new Exception("Consorcio no encontrado");
        }
        return consorcio.get();
    }
    private void validateTotal(Double banco, Double efectivo, Double fondoAdm, Double total) throws Exception {
        Double suma = banco + efectivo + fondoAdm;
        if(suma != total){
            throw new Exception("El reparto valores es de $"+ suma +" mientras que el total es de $"+ total);

        }
    }


    // MAP DTO TO ENTITY
    private EstadoCuenta mapToEstadoCuentaEntity(EstadoCuentaCreateDTO dto) throws Exception {
        validateTotal(dto.getBanco(), dto.getEfectivo(), dto.getFondoAdm(), dto.getTotal());
        Consorcio consorcio = validateConsorcio(dto.getIdConsorcio());

        EstadoCuenta ec = new EstadoCuenta();

        ec.setConsorcio(consorcio);
        ec.setEfectivo(dto.getEfectivo());
        ec.setBanco(dto.getBanco());
        ec.setFondoAdm(dto.getFondoAdm());
        ec.setTotal(dto.getTotal());

        return ec;
    }
    private EstadoCuenta mapToEstadoCuentaEntityUpdate(EstadoCuentaDTO dto) throws Exception {
        validateTotal(dto.getBanco(), dto.getEfectivo(), dto.getFondoAdm(), dto.getTotal());

        EstadoCuenta ec = new EstadoCuenta();

        ec.setIdEstadoCuenta(dto.getIdEstadoCuenta());
        ec.setEfectivo(dto.getEfectivo());
        ec.setBanco(dto.getBanco());
        ec.setFondoAdm(dto.getFondoAdm());
        ec.setTotal(dto.getTotal());

        return ec;
    }
    // MAP ENTITY TO DTO
    private EstadoCuentaDTO mapToEstadoCuentaDTO (EstadoCuenta ea) {
        EstadoCuentaDTO dto = new EstadoCuentaDTO();

        dto.setIdEstadoCuenta(ea.getIdEstadoCuenta());
        dto.setEfectivo(ea.getEfectivo());
        dto.setBanco(ea.getBanco());
        dto.setFondoAdm(ea.getFondoAdm());
        dto.setTotal(ea.getTotal());

        return dto;
    }



}
