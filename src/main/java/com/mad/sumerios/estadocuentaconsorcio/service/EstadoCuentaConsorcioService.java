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

        ea.setEfectivo(eaUpdated.getEfectivo());
        ea.setBanco(eaUpdated.getBanco());
        ea.setFondoAdm(eaUpdated.getFondoAdm());
        ea.setTotal(eaUpdated.getTotal());
        ea.setTotalAlCierre(eaUpdated.getTotalAlCierre());

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
    public void modificarEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Egreso egresoViejo, Egreso egresoNuevo) {
        if (egresoViejo.getFormaPago().equals(egresoNuevo.getFormaPago())) {
            actualizarTotalEstadoCuentaEgreso(estadoCuentaConsorcio, egresoViejo.getTotalFinal(), egresoNuevo.getTotalFinal());
            actualizarCajaPorCambioMontoEgreso(estadoCuentaConsorcio, egresoViejo.getFormaPago(), egresoViejo.getTotalFinal(), egresoNuevo.getTotalFinal());
        } else if (egresoViejo.getTotalFinal().equals(egresoNuevo.getTotalFinal())) {
            actualizarCajaPorCambioFormaPagoEgreso(estadoCuentaConsorcio, egresoViejo, egresoNuevo);
        } else {
            actualizarTotalEstadoCuentaEgreso(estadoCuentaConsorcio, egresoViejo.getTotalFinal(), egresoNuevo.getTotalFinal());
            actualizarCajaPorCambioFormaPagoEgreso(estadoCuentaConsorcio, egresoViejo, egresoNuevo);
        }
    }

    private void actualizarCajaPorCambioMontoEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, FormaPago formaPago, Double totalViejo, Double totalNuevo) {
        double diferenciaNuevoMayor = totalNuevo - totalViejo;
        double diferenciaViejoMayor = totalViejo - totalNuevo;

        if (formaPago.equals(FormaPago.EFECTIVO)) {
            if(totalNuevo > totalViejo){
                estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - diferenciaNuevoMayor);
            } else if (totalNuevo < totalViejo){
                estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferenciaViejoMayor);
            }
        } else if (formaPago.equals(FormaPago.BANCO)) {
            if (totalNuevo > totalViejo) {
                estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - diferenciaNuevoMayor);
            } else if (totalNuevo < totalViejo) {
                estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferenciaViejoMayor);
            }
        }
    }
    private void actualizarTotalEstadoCuentaEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Double totalViejo, Double totalNuevo) {
        if (totalNuevo > totalViejo) {
            // Descontar la diferencia adicional
            Double diferencia = totalNuevo - totalViejo;
            estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - diferencia);
        } else if (totalNuevo < totalViejo) {
            // Reintegrar lo que se quitó de más
            Double diferencia = totalViejo - totalNuevo;
            estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
        }
    }
    private void actualizarCajaPorCambioFormaPagoEgreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Egreso egresoViejo, Egreso egresoNuevo) {
        if (egresoViejo.getFormaPago().equals(FormaPago.EFECTIVO)) {
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + egresoViejo.getTotalFinal());
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - egresoNuevo.getTotalFinal());
        } else {
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + egresoViejo.getTotalFinal());
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - egresoNuevo.getTotalFinal());
        }
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
    public void modificarIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Ingreso ingresoViejo, Ingreso ingresoNuevo) {
        if (ingresoViejo.getFormaPago().equals(ingresoNuevo.getFormaPago())) {
            actualizarTotalEstadoCuentaIngreso(estadoCuentaConsorcio, ingresoViejo.getValor(), ingresoNuevo.getValor());
            actualizarCajaPorCambioMontoIngreso(estadoCuentaConsorcio, ingresoViejo.getFormaPago(), ingresoViejo.getValor(), ingresoNuevo.getValor());
        } else if (ingresoViejo.getValor()== ingresoNuevo.getValor()) {
            actualizarCajaPorCambioFormaPagoIngreso(estadoCuentaConsorcio, ingresoViejo, ingresoNuevo);
        } else {
            actualizarTotalEstadoCuentaIngreso(estadoCuentaConsorcio, ingresoViejo.getValor(), ingresoNuevo.getValor());
            actualizarCajaPorCambioFormaPagoIngreso(estadoCuentaConsorcio, ingresoViejo, ingresoNuevo);
        }

        estadoCuentaRepository.save(estadoCuentaConsorcio);
    }

    private void actualizarTotalEstadoCuentaIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Double totalViejo, Double totalNuevo) {
        if (totalNuevo > totalViejo) {
            Double diferencia = totalNuevo - totalViejo;
            estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
        } else if (totalNuevo < totalViejo) {
            Double diferencia = totalViejo - totalNuevo;
            estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - diferencia);
        }
    }
    private void actualizarCajaPorCambioMontoIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, FormaPago formaPago, double totalViejo, double totalNuevo) {
        double diferenciaNuevoMayor = totalNuevo - totalViejo;
        double diferenciaViejoMayor = totalViejo - totalNuevo;

        if (formaPago.equals(FormaPago.EFECTIVO)) {
            if(totalNuevo > totalViejo){
                estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferenciaNuevoMayor);
            } else if (totalNuevo < totalViejo){
                estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - diferenciaViejoMayor);
            }
        } else if (formaPago.equals(FormaPago.BANCO)) {
            if (totalNuevo > totalViejo) {
                estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferenciaNuevoMayor);
            } else if (totalNuevo < totalViejo) {
                estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - diferenciaViejoMayor);
            }
        }
    }
    private void actualizarCajaPorCambioFormaPagoIngreso(EstadoCuentaConsorcio estadoCuentaConsorcio, Ingreso ingresoViejo, Ingreso ingresoNuevo) {
        if (ingresoViejo.getFormaPago().equals(FormaPago.EFECTIVO)) {
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - ingresoViejo.getValor());
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + ingresoNuevo.getValor());
        } else {
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - ingresoViejo.getValor());
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + ingresoNuevo.getValor());
        }
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
    public void modificarGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, GastoParticular gpViejo, GastoParticular gpNuevo) {
        if(gpNuevo.getPagoConsorcio()){
            if (gpViejo.getFormaPago().equals(gpNuevo.getFormaPago())) {
                actualizarTotalEstadoCuentaGastoParticular(estadoCuentaConsorcio, gpViejo.getTotalFinal(), gpNuevo.getTotalFinal());
                actualizarCajaPorCambioMontoGastoParticular(estadoCuentaConsorcio, gpViejo.getFormaPago(), gpViejo.getTotalFinal(), gpNuevo.getTotalFinal());
            } else if (gpViejo.getTotalFinal().equals(gpNuevo.getTotalFinal())) {
                actualizarCajaPorCambioFormaPagoGastoParticular(estadoCuentaConsorcio, gpViejo, gpNuevo);
            } else {
                actualizarTotalEstadoCuentaGastoParticular(estadoCuentaConsorcio, gpViejo.getTotalFinal(), gpNuevo.getTotalFinal());
                actualizarCajaPorCambioFormaPagoGastoParticular(estadoCuentaConsorcio, gpViejo, gpNuevo);
            }
        } else {
            revertirGastoParticular(estadoCuentaConsorcio, gpViejo);
        }
    }

    private void actualizarCajaPorCambioMontoGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, FormaPago formaPago, Double gpViejo, Double GpNuevo) {
        double diferenciaNuevoMayor = GpNuevo - gpViejo;
        double diferenciaViejoMayor = gpViejo - GpNuevo;

        if (formaPago.equals(FormaPago.EFECTIVO)) {
            if(GpNuevo > gpViejo){
                estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - diferenciaNuevoMayor);
            } else if (GpNuevo < gpViejo){
                estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferenciaViejoMayor);
            }
        } else if (formaPago.equals(FormaPago.BANCO)) {
            if (GpNuevo > gpViejo) {
                estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - diferenciaNuevoMayor);
            } else if (GpNuevo < gpViejo) {
                estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferenciaViejoMayor);
            }
        }
    }
    private void actualizarTotalEstadoCuentaGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, Double gpViejo, Double GpNuevo) {
        if (GpNuevo > gpViejo) {
            // Descontar la diferencia adicional
            Double diferencia = GpNuevo - gpViejo;
            estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - diferencia);
        } else if (GpNuevo < gpViejo) {
            // Reintegrar lo que se quitó de más
            Double diferencia = gpViejo - GpNuevo;
            estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
        }
    }
    private void actualizarCajaPorCambioFormaPagoGastoParticular(EstadoCuentaConsorcio estadoCuentaConsorcio, GastoParticular gpViejo, GastoParticular GpNuevo) {
        if (gpViejo.getFormaPago().equals(FormaPago.EFECTIVO)) {
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + gpViejo.getTotalFinal());
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - GpNuevo.getTotalFinal());
        } else {
            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + gpViejo.getTotalFinal());
            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - GpNuevo.getTotalFinal());
        }
    }

    // PagoUF
    // Sumar
    public void sumarPagoUF(EstadoCuentaConsorcioDTO dto, PagoUF pago){
        EstadoCuentaConsorcio estadoCuentaConsorcio = estadoCuentaRepository.findById(dto.getIdEstadoCuentaConsorcio()).get();

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
//    public void modificarPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pagoViejo, PagoUF pagoNuevo){
//        if(pagoViejo.getFormaPago() == pagoNuevo.getFormaPago()){
//            updateMismaFormaPagoPagoUF(estadoCuentaConsorcio, pagoViejo, pagoNuevo);
//        } else{
//            updateDistintaPagoUF(estadoCuentaConsorcio, pagoViejo, pagoNuevo);
//        }
//    }
//    private void updateDistintaPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pagoViejo, PagoUF pagoNuevo) {
//        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() - pagoViejo.getValor());
//        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + pagoNuevo.getValor());
//
//        if(pagoViejo.getFormaPago().equals(FormaPago.EFECTIVO)){
//            // reversion gasto original
//            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() - pagoViejo.getValor());
//            // descuento gasto nuevo
//            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + pagoNuevo.getValor());
//        } else{
//            // reversion gasto original
//            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() - pagoViejo.getValor());
//            // descuento gasto nuevo
//            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + pagoNuevo.getValor());
//        }
//
//        estadoCuentaRepository.save(estadoCuentaConsorcio);
//    }
//    private void updateMismaFormaPagoPagoUF(EstadoCuentaConsorcio estadoCuentaConsorcio, PagoUF pagoViejo, PagoUF pagoNuevo) {
//        Double diferencia = pagoNuevo.getValor() - pagoViejo.getValor();
//        estadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal() + diferencia);
//        if(pagoViejo.getFormaPago() == FormaPago.EFECTIVO){
//            estadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo() + diferencia);
//        } else{
//            estadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco() + diferencia);
//        }
//
//        estadoCuentaRepository.save(estadoCuentaConsorcio);
//    }

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
        Consorcio consorcio = validateConsorcio(dto.getIdConsorcio());
        if (consorcio.getEstadoCuentaConsorcio() != null) {
            throw new Exception("El consorcio ya tiene un estado de cuenta asociado.");
        }

        EstadoCuentaConsorcio ec = new EstadoCuentaConsorcio();

        ec.setConsorcio(consorcio);
        ec.setEfectivo(0.0);
        ec.setBanco(0.0);
        ec.setFondoAdm(0.0);
        ec.setTotal(0.0);
        ec.setTotalAlCierre(0.0);

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
        ec.setTotalAlCierre(dto.getTotalAlCierre());

        return ec;
    }
    // MAP ENTITY TO DTO
    public EstadoCuentaConsorcioDTO mapToEstadoCuentaDTO (EstadoCuentaConsorcio ea) {
        EstadoCuentaConsorcioDTO dto = new EstadoCuentaConsorcioDTO();

        dto.setIdEstadoCuentaConsorcio(ea.getIdEstadoCuentaConsorcio());
        dto.setEfectivo(ea.getEfectivo());
        dto.setBanco(ea.getBanco());
        dto.setFondoAdm(ea.getFondoAdm());
        dto.setTotal(ea.getTotal());
        dto.setTotalAlCierre(ea.getTotalAlCierre());

        return dto;
    }


    public void actualizarTotalAlCierre(Long idEstadoCuentaConsorcio, Double total) throws Exception {
        EstadoCuentaConsorcio ecc = estadoCuentaRepository.findById(idEstadoCuentaConsorcio)
                .orElseThrow(()-> new Exception("Estado de cuenta del consorcio no encontrado"));

        ecc.setTotalAlCierre(total);

        estadoCuentaRepository.save(ecc);
    }
}
