package com.mad.sumerios.estadocuentaconsorcio.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioCreateDTO;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioDTO;
import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import com.mad.sumerios.estadocuentaconsorcio.repository.IEstadoCuentaConsorcioRepository;
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
public class EstadoCuentaConsorcioService {

    private final IEstadoCuentaConsorcioRepository estadoCuentaRepository;
    private final IConsorcioRepository consorcioRepository;

    @Autowired
    public EstadoCuentaConsorcioService(IEstadoCuentaConsorcioRepository estadoCuentaRepository,
                                        IConsorcioRepository consorcioRepository){
        this.estadoCuentaRepository = estadoCuentaRepository;
        this.consorcioRepository = consorcioRepository;
    }

    //  CREATE ESTADO CUENTA
    public void createEstadoCuenta (EstadoCuentaConsorcioCreateDTO dto) throws Exception{
        estadoCuentaRepository.save(mapToEstadoCuentaEntity(dto));
    }

    //  UPDATE ESTADO CUENTA
    public void updateEstadoCuenta (Long idEstadoCuenta, EstadoCuentaConsorcioDTO dto) throws Exception {
        EstadoCuentaConsorcio ea = estadoCuentaRepository.findById(idEstadoCuenta)
                .orElseThrow(()-> new Exception("Estado de cuenta no encontrado con el ID: " + idEstadoCuenta));

        EstadoCuentaConsorcio eaUpdated = mapToEstadoCuentaEntityUpdate(dto);

        ea.setEfectivo(dto.getEfectivo());
        ea.setBanco(dto.getBanco());
        ea.setFondoAdm(dto.getFondoAdm());
        ea.setTotal(dto.getTotal());

        estadoCuentaRepository.save(ea);
    }

    //  GET
    //  all
    public List<EstadoCuentaConsorcioDTO> getAllEstadoCuenta (){
        List<EstadoCuentaConsorcio> eas = estadoCuentaRepository.findAll();
        return eas.stream().map(this::mapToEstadoCuentaDTO).collect(Collectors.toList());
    }
    //  by consorcio
    public EstadoCuentaConsorcioDTO getByIdConsorcio (Long idConsorcio){
        EstadoCuentaConsorcio ea = estadoCuentaRepository.findByConsorcio_idConsorcio(idConsorcio);
        return mapToEstadoCuentaDTO(ea);
    }


    // METODOS

    // Egreso
    // Restar
    public void restarEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Egreso egreso){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - egreso.getTotalFinal());
        if(egreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - egreso.getTotalFinal());
        } else {
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - egreso.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Revertir
    public void revertirEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Egreso egreso){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + egreso.getTotalFinal());
        if(egreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + egreso.getTotalFinal());
        } else {
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + egreso.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Modificar
    public void modificarEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Egreso egresoViejo, Egreso egresoNuevo){
        if(egresoViejo.getFormaPago() == egresoNuevo.getFormaPago()){
            updateMismaFormaPagoEgreso(estadoCuentaConsorcio, egresoViejo, egresoNuevo);
        } else{
            updateDistintaFormaPagoEgreso(estadoCuentaConsorcio, egresoViejo, egresoNuevo);
        }
    }
    private void updateDistintaFormaPagoEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Egreso egresoViejo, Egreso egresoNuevo) {
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + egresoViejo.getTotalFinal());
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - egresoNuevo.getTotalFinal());

        if(egresoViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + egresoViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - egresoNuevo.getTotalFinal());
        } else{
            // reversion gasto original
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + egresoViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - egresoNuevo.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    private void updateMismaFormaPagoEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Egreso egresoViejo, Egreso egresoNuevo) {
        Double diferencia = egresoNuevo.getTotalFinal() - egresoViejo.getTotalFinal();
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
        if(egresoViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferencia);
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }

    // Gasto particular
    // Restar
    public void restarGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, GastoParticular gastoParticular){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - gastoParticular.getTotalFinal());
        if(gastoParticular.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - gastoParticular.getTotalFinal());
        } else {
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - gastoParticular.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Revertir
    public void revertirGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, GastoParticular gastoParticular){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + gastoParticular.getTotalFinal());
        if(gastoParticular.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + gastoParticular.getTotalFinal());
        } else {
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + gastoParticular.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Modificar
    public void modificarGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, GastoParticular gastoParticularViejo, GastoParticular gastoParticularNuevo){
        if(gastoParticularViejo.getFormaPago() == gastoParticularNuevo.getFormaPago()){
            updateMismaFormaPagoGastoParticular(estadoCuentaConsorcio, gastoParticularViejo, gastoParticularNuevo);
        } else{
            updateDistintaFormaPagoGastoParticular(estadoCuentaConsorcio, gastoParticularViejo, gastoParticularNuevo);
        }
    }
    private void updateDistintaFormaPagoGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, GastoParticular gastoParticularViejo, GastoParticular gastoParticularNuevo) {
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + gastoParticularViejo.getTotalFinal());
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - gastoParticularNuevo.getTotalFinal());

        if(gastoParticularViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + gastoParticularViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - gastoParticularNuevo.getTotalFinal());
        } else{
            // reversion gasto original
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + gastoParticularViejo.getTotalFinal());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - gastoParticularNuevo.getTotalFinal());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    private void updateMismaFormaPagoGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, GastoParticular gastoParticularViejo, GastoParticular gastoParticularNuevo) {
        Double diferencia = gastoParticularNuevo.getTotalFinal() - gastoParticularViejo.getTotalFinal();
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
        if(gastoParticularViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferencia);
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }


    // Ingreso
    // Sumar
    public void sumarIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Ingreso ingreso){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + ingreso.getValor());
        if(ingreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + ingreso.getValor());
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + ingreso.getValor());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Revertir
    public void revertirIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Ingreso ingreso){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - ingreso.getValor());
        if(ingreso.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - ingreso.getValor());
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - ingreso.getValor());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Modificar
    public void modificarIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Ingreso ingresoViejo, Ingreso ingresoNuevo){
        if(ingresoViejo.getFormaPago() == ingresoNuevo.getFormaPago()){
            updateMismaFormaPagoIngreso(estadoCuentaConsorcio, ingresoViejo, ingresoNuevo);
        } else{
            updateDistintaFormaPagoIngreso(estadoCuentaConsorcio, ingresoViejo, ingresoNuevo);
        }
    }
    private void updateDistintaFormaPagoIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Ingreso ingresoViejo, Ingreso ingresoNuevo) {
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - ingresoViejo.getValor());
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + ingresoNuevo.getValor());

        if(ingresoViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - ingresoViejo.getValor());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + ingresoNuevo.getValor());
        } else{
            // reversion gasto original
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - ingresoViejo.getValor());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + ingresoNuevo.getValor());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    private void updateMismaFormaPagoIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Ingreso ingresoViejo, Ingreso ingresoNuevo) {
        Double diferencia = ingresoNuevo.getValor() - ingresoViejo.getValor();
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
        if(ingresoViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferencia);
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }


    // PagoUF
    // Sumar
    public void sumarPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pago){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + pago.getValor());
        if(pago.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + pago.getValor());
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + pago.getValor());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Revertir
    public void revertirPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pago){
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - pago.getValor());
        if(pago.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - pago.getValor());
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - pago.getValor());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    // Modificar
    public void modificarPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pagoViejo, PagoUF pagoNuevo){
        if(pagoViejo.getFormaPago() == pagoNuevo.getFormaPago()){
            updateMismaFormaPagoPagoUF(estadoCuentaConsorcio, pagoViejo, pagoNuevo);
        } else{
            updateDistintaPagoUF(estadoCuentaConsorcio, pagoViejo, pagoNuevo);
        }
    }
    private void updateDistintaPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pagoViejo, PagoUF pagoNuevo) {
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - pagoViejo.getValor());
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + pagoNuevo.getValor());

        if(pagoViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
            // reversion gasto original
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - pagoViejo.getValor());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + pagoNuevo.getValor());
        } else{
            // reversion gasto original
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - pagoViejo.getValor());
            // descuento gasto nuevo
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + pagoNuevo.getValor());
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }
    private void updateMismaFormaPagoPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pagoViejo, PagoUF pagoNuevo) {
        Double diferencia = pagoNuevo.getValor() - pagoViejo.getValor();
        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
        if(pagoViejo.getFormaPago() == FormaPago.EFECTIVO){
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferencia);
        } else{
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferencia);
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
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
        double suma = banco + efectivo + fondoAdm;
        if(suma != total){
            throw new Exception("El reparto valores es de $"+ suma +" mientras que el total es de $"+ total);

        }
    }


    // MAP DTO TO ENTITY
    private EstadoCuentaConsorcio mapToEstadoCuentaEntity(EstadoCuentaConsorcioCreateDTO dto) throws Exception {
        validateTotal(dto.getBanco(), dto.getEfectivo(), dto.getFondoAdm(), dto.getTotal());

        Consorcio consorcio = validateConsorcio(dto.getIdConsorcio());
        if (consorcio.getEstadoCuentaConsorcio() != null) {
            throw new Exception("El consorcio ya tiene un estado de cuenta asociado.");
        }

        EstadoCuentaConsorcio ec = new EstadoCuentaConsorcio();

        ec.setConsorcio(consorcio);
        ec.setEfectivo(dto.getEfectivo());
        ec.setBanco(dto.getBanco());
        ec.setFondoAdm(dto.getFondoAdm());
        ec.setTotal(dto.getTotal());

        consorcio.setEstadoCuentaConsorcio(ec);

        return ec;
    }
    private EstadoCuentaConsorcio mapToEstadoCuentaEntityUpdate(EstadoCuentaConsorcioDTO dto) throws Exception {
        validateTotal(dto.getBanco(), dto.getEfectivo(), dto.getFondoAdm(), dto.getTotal());

        EstadoCuentaConsorcio ec = new EstadoCuentaConsorcio();

        ec.setIdEstadoCuentaConsorcio(dto.getIdEstadoCuentaConsorcio());
        ec.setEfectivo(dto.getEfectivo());
        ec.setBanco(dto.getBanco());
        ec.setFondoAdm(dto.getFondoAdm());
        ec.setTotal(dto.getTotal());

        return ec;
    }
    // MAP ENTITY TO DTO
    private EstadoCuentaConsorcioDTO mapToEstadoCuentaDTO (EstadoCuentaConsorcio ea) {
        EstadoCuentaConsorcioDTO dto = new EstadoCuentaConsorcioDTO();

        dto.setIdEstadoCuentaConsorcio(ea.getIdEstadoCuentaConsorcio());
        dto.setEfectivo(ea.getEfectivo());
        dto.setBanco(ea.getBanco());
        dto.setFondoAdm(ea.getFondoAdm());
        dto.setTotal(ea.getTotal());

        return dto;
    }



}
