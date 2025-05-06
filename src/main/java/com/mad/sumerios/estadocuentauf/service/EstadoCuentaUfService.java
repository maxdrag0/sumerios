package com.mad.sumerios.estadocuentauf.service;

import com.mad.sumerios.copiaestadocuentauf.dto.CopiaEstadoCuentaUfDTO;
import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfCreateDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfUpdatePeriodo;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.estadocuentauf.repository.IEstadoCuentaUfRepository;
import com.mad.sumerios.copiaestadocuentauf.service.CopiaEstadoCuentaUfService;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstadoCuentaUfService {

    private final IEstadoCuentaUfRepository estadoCuentaUfRepository;
    private final IUnidadFuncionalRepository unidadFuncionalRepository;
    private final CopiaEstadoCuentaUfService copiaEstadoCuentaUfService;

    @Autowired
    public EstadoCuentaUfService (IEstadoCuentaUfRepository estadoCuentaUfRepository,
                                  IUnidadFuncionalRepository unidadFuncionalRepository,
                                  CopiaEstadoCuentaUfService copiaEstadoCuentaUfService){
        this.estadoCuentaUfRepository = estadoCuentaUfRepository;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
        this.copiaEstadoCuentaUfService = copiaEstadoCuentaUfService;
    }

    //  CREATE ESTADO CUENTA
    public void createEstadoCuentaUf (EstadoCuentaUfCreateDTO dto) throws Exception{
        estadoCuentaUfRepository.save(mapToEstadoCuentaUfEntity(dto));
    }
    //  UPDATE ESTADO CUENTA
    public void updateEstadoCuentaUf (Long idEstadoCuentaUf, EstadoCuentaUfDTO dto) throws Exception {
        EstadoCuentaUf ec = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()-> new Exception("Estado de cuenta no encontrado con el ID: " + idEstadoCuentaUf));

        EstadoCuentaUf ecUpdated = mapToEstadoCuentaUfEntityUpdate(dto);
        System.out.println();
        ec.setPeriodo(ecUpdated.getPeriodo());
        ec.setTotalMesPrevio(ecUpdated.getTotalMesPrevio());
        ec.setDeuda(ecUpdated.getDeuda());
        ec.setIntereses(ecUpdated.getIntereses());
        ec.setTotalA(ecUpdated.getTotalA());
        ec.setTotalB(ecUpdated.getTotalB());
        ec.setTotalC(ecUpdated.getTotalC());
        ec.setTotalD(ecUpdated.getTotalD());
        ec.setTotalE(ecUpdated.getTotalE());
        ec.setGastoParticular(ecUpdated.getGastoParticular());
        ec.setRedondeo(ecUpdated.getRedondeo());
        ec.setTotalExpensa(ecUpdated.getTotalExpensa());
        ec.setSegundoVencimientoActivo(ecUpdated.getSegundoVencimientoActivo());
        ec.setSegundoVencimiento(ecUpdated.getSegundoVencimiento());
        ec.setSaldoFinal(ecUpdated.getSaldoFinal());
        ec.setSaldoExpensa(ecUpdated.getSaldoExpensa());
        ec.setSaldoIntereses(ecUpdated.getSaldoIntereses());

        estadoCuentaUfRepository.save(ec);
    }

    public void updatePeriodos (EstadoCuentaUfUpdatePeriodo dto) throws Exception {
        List<EstadoCuentaUfDTO> ecufs = this.getEstadoCuentaUfs(dto.getUnidadFuncionalList());

        for(EstadoCuentaUfDTO ecuf : ecufs){
            EstadoCuentaUf ec = estadoCuentaUfRepository.findById(ecuf.getIdEstadoCuentaUf())
                    .orElseThrow(() -> new Exception("Estado de cuenta no encontrado para ID: " + ecuf.getIdEstadoCuentaUf()));

            ec.setPeriodo(dto.getPeriodo());

            estadoCuentaUfRepository.save(ec);
        }

    }
    //  GET
    //  BY id
    public EstadoCuentaUfDTO getEstadoCuentaUfById (Long idEstadoCuentaUf) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()-> new Exception("Estado de cuenta de UF no encontrado."));
        return mapToEstadoCuentaUfDTO(estadoCuentaUf);
    }
    //  BY UF
    public EstadoCuentaUfDTO getEstadoCuentaByUf (Long idUf){
        return mapToEstadoCuentaUfDTO(estadoCuentaUfRepository.findByUnidadFuncional_idUf(idUf));
    }

    // by ufs
    public List<EstadoCuentaUfDTO> getEstadoCuentaUfs(List<UnidadFuncionalResponseDTO> unidadFuncionalList){
        List<EstadoCuentaUfDTO> ecufs = new ArrayList<>();
        for(UnidadFuncionalResponseDTO dto : unidadFuncionalList){
            ecufs.add(dto.getEstadoCuentaUfDTO());
        }
        return ecufs;
    }

    // METODO PARA RESTABLECER ESTADO DE CUENTA CUANDO SE ELIMINA UNA EXPENSA
    public void restablecerPeriodoPrevio(Long idEstadoCuentaUf) throws Exception {

        EstadoCuentaUfDTO estadoActual = this.getEstadoCuentaUfById(idEstadoCuentaUf);
        CopiaEstadoCuentaUfDTO copiaEstadoPeriodoPrevio = copiaEstadoCuentaUfService.getCopiasEstadoCuentaUfByIdEstadoCuentaUfAndPeriodo(
                idEstadoCuentaUf,
                estadoActual.getPeriodo().minusMonths(1));
        if(copiaEstadoPeriodoPrevio == null){
            throw new Exception("Copia no encontrada");
        }


        // COPIO LOS DATOS VIEJOS AL ESTADO ACTUAL
        estadoActual.setPeriodo(copiaEstadoPeriodoPrevio.getPeriodo());
        if(copiaEstadoPeriodoPrevio.getTotalMesPrevio() == null){
            estadoActual.setTotalMesPrevio(0.0);

        } else{
            estadoActual.setTotalMesPrevio(copiaEstadoPeriodoPrevio.getTotalMesPrevio());

        }
        estadoActual.setDeuda(copiaEstadoPeriodoPrevio.getDeuda());
        estadoActual.setIntereses(copiaEstadoPeriodoPrevio.getIntereses());
        estadoActual.setTotalA(copiaEstadoPeriodoPrevio.getTotalA());
        estadoActual.setTotalB(copiaEstadoPeriodoPrevio.getTotalB());
        estadoActual.setTotalC(copiaEstadoPeriodoPrevio.getTotalC());
        estadoActual.setTotalD(copiaEstadoPeriodoPrevio.getTotalD());
        estadoActual.setTotalE(copiaEstadoPeriodoPrevio.getTotalE());
        estadoActual.setGastoParticular(copiaEstadoPeriodoPrevio.getGastoParticular());

        if(copiaEstadoPeriodoPrevio.getRedondeo() == null){
            estadoActual.setRedondeo(0.0);

        } else{
            estadoActual.setRedondeo(copiaEstadoPeriodoPrevio.getRedondeo());
        }

        estadoActual.setTotalExpensa(copiaEstadoPeriodoPrevio.getTotalExpensa());

        if(copiaEstadoPeriodoPrevio.getSegundoVencimientoActivo() == null){
            estadoActual.setSegundoVencimientoActivo(false);
        } else {
            estadoActual.setSegundoVencimientoActivo(copiaEstadoPeriodoPrevio.getSegundoVencimientoActivo());
        }

        if(copiaEstadoPeriodoPrevio.getSegundoVencimiento() == null){
            estadoActual.setSegundoVencimiento(0.0);
        } else {
            estadoActual.setSegundoVencimiento(copiaEstadoPeriodoPrevio.getSegundoVencimiento());
        }
        estadoActual.setSaldoFinal(copiaEstadoPeriodoPrevio.getSaldoFinal());
        estadoActual.setSaldoExpensa(copiaEstadoPeriodoPrevio.getSaldoExpensa());
        estadoActual.setSaldoIntereses(copiaEstadoPeriodoPrevio.getSaldoIntereses());
        this.updateEstadoCuentaUf(idEstadoCuentaUf, estadoActual);
        // ELIMINO LA COPIA
        copiaEstadoCuentaUfService.deleteCopiaEstadoCuentaUf(copiaEstadoPeriodoPrevio.getIdCopiaEstadoCuentaUf());
    }

    // VALIDACIONES Y AUX

//    private void validateValor(Double deuda,
//                               Double intereses,
//                               Double totalA,
//                               Double totalB,
//                               Double totalC,
//                               Double totalD,
//                               Double totalE,
//                               Double gastoParticular,
//                               Double totalFinal) throws Exception {
//        double suma = deuda+intereses+totalA+totalB+totalC+totalD+totalE+gastoParticular;
//        if(suma != totalFinal) {
//            throw new Exception(
//                    "El reparto valores es de $"+ suma +" mientras que el total es de $"+ totalFinal);
//        }
//    }
    private UnidadFuncional validateUf(Long idUf) throws Exception {
        Optional<UnidadFuncional> uf = unidadFuncionalRepository.findById(idUf);
        if(uf.isEmpty()){
            throw new Exception("Unidad funcional no encontrada");
        }

        return uf.get();
    }
    public void aplicarDeudaEIntereses(long idEstadoCuentaUf, double porcentajeIntereses) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(() -> new Exception("Estado de cuenta no encontrado"));


        double intereses = (estadoCuentaUf.getSaldoExpensa() * porcentajeIntereses) / 100;

        estadoCuentaUf.setTotalMesPrevio(estadoCuentaUf.getTotalExpensa());
        estadoCuentaUf.setDeuda(estadoCuentaUf.getSaldoFinal());
        estadoCuentaUf.setIntereses(intereses);
        // Modifico saldo expensa e saldo intereses
        estadoCuentaUf.setSaldoFinal(0.0);
        estadoCuentaUf.setSaldoIntereses(intereses);

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }
    public void aplicarValorCategoria(long idEstadoCuentaUf,
                             double porcentaje,
                             double total,
                             CategoriaEgreso categoria) throws Exception {

        double totalAAplicar = (porcentaje > 0) ? (total * porcentaje) / 100 : 0;

        if (totalAAplicar>0){
            EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                    .orElseThrow(()-> new Exception("Estado de cuenta no encontrado."));
            categoria.aplicar(estadoCuentaUf, totalAAplicar);
            estadoCuentaUfRepository.save(estadoCuentaUf);
        }

    }
    public void aplicarValorGastoParticular(long idEstadoCuentaUf, double valor) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()->new Exception("Estado de cuenta no encontrado"));

        double nuevoValor = estadoCuentaUf.getGastoParticular() + valor;
        estadoCuentaUf.setGastoParticular(nuevoValor);

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }

    public void aplicarTotalFinal(long idEstadoCuentaUf) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()->new Exception("Estado de cuenta no encontrado"));

        double valorDeExpensa = estadoCuentaUf.getTotalA() +
                                estadoCuentaUf.getTotalB() +
                                estadoCuentaUf.getTotalC() +
                                estadoCuentaUf.getTotalD() +
                                estadoCuentaUf.getTotalE() +
                                estadoCuentaUf.getGastoParticular();

        double valorDeudaEIntereses = estadoCuentaUf.getDeuda() + estadoCuentaUf.getIntereses();
        double valorFinal = valorDeExpensa+valorDeudaEIntereses;
        double redondeo = diferenciaRedondeo(valorFinal);

        estadoCuentaUf.setRedondeo(redondeo);
        estadoCuentaUf.setSaldoExpensa(estadoCuentaUf.getSaldoExpensa() + valorDeExpensa);
        estadoCuentaUf.setSaldoFinal(valorFinal+redondeo);
        estadoCuentaUf.setTotalExpensa(valorFinal+redondeo);

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }

    public void calcularSegundoVencimiento(Long idEstadoCuentaUf, Double porcentajeSegundoVencimiento) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()->new Exception("Estado de cuenta no encontrado"));

        estadoCuentaUf.setSegundoVencimiento((estadoCuentaUf.getTotalExpensa() * porcentajeSegundoVencimiento) / 100);
        estadoCuentaUf.setSegundoVencimientoActivo(false);
    }

    public void imputarSegundoVencimiento(Long idEstadoCuentaUf) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()->new Exception("Estado de cuenta no encontrado"));

        double diferencia = estadoCuentaUf.getSegundoVencimiento() - estadoCuentaUf.getTotalExpensa();
        estadoCuentaUf.setSegundoVencimientoActivo(true);
        estadoCuentaUf.setSaldoFinal(estadoCuentaUf.getSaldoFinal() + diferencia);
        estadoCuentaUf.setSaldoIntereses(estadoCuentaUf.getSaldoIntereses() + diferencia);
    }

    public void desimputarSegundoVencimiento(Long idEstadoCuentaUf) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()->new Exception("Estado de cuenta no encontrado"));

        double diferencia = estadoCuentaUf.getSegundoVencimiento() - estadoCuentaUf.getTotalExpensa();
        estadoCuentaUf.setSegundoVencimientoActivo(false);
        estadoCuentaUf.setSaldoFinal(estadoCuentaUf.getSaldoFinal() - diferencia);
        estadoCuentaUf.setSaldoIntereses(estadoCuentaUf.getSaldoIntereses() - diferencia);
    }

    private double diferenciaRedondeo(double numero) {
        double numeroRedondeado = Math.ceil(numero);
        return numeroRedondeado - numero;
    }

    // MAP DTO TO ENTITY
    private EstadoCuentaUf mapToEstadoCuentaUfEntity(EstadoCuentaUfCreateDTO dto) throws Exception {
        UnidadFuncional uf = validateUf(dto.getIdUf());

        if(uf.getEstadoCuentaUf() != null){
            throw new Exception("La unidad funcional ya tiene un estado de cuenta asociado.");
        }

        EstadoCuentaUf ec = new EstadoCuentaUf();

        ec.setUnidadFuncional(uf);
        if(dto.getPeriodo() != null)  {
            ec.setPeriodo(dto.getPeriodo());
        }
        ec.setTotalMesPrevio(0.0);
        ec.setDeuda(0.0);
        ec.setIntereses(0.0);
        ec.setTotalA(0.0);
        ec.setTotalB(0.0);
        ec.setTotalC(0.0);
        ec.setTotalD(0.0);
        ec.setTotalE(0.0);
        ec.setGastoParticular(0.0);
        ec.setRedondeo(0.0);
        ec.setTotalExpensa(0.0);
        ec.setSegundoVencimientoActivo(false);
        ec.setSegundoVencimiento(0.0);
        ec.setSaldoFinal(0.0);
        ec.setSaldoExpensa(0.0);
        ec.setSaldoIntereses(0.0);

        uf.setEstadoCuentaUf(ec);

        return ec;
    }
    private EstadoCuentaUf mapToEstadoCuentaUfEntityUpdate(EstadoCuentaUfDTO dto) throws Exception {
        if(unidadFuncionalRepository.findById(dto.getIdUf()).isEmpty()){
            throw new Exception("Unidad funcional no encontrada");

        }

        EstadoCuentaUf ec = new EstadoCuentaUf();


        ec.setPeriodo(dto.getPeriodo());
        ec.setTotalMesPrevio(dto.getTotalMesPrevio());
        ec.setDeuda(dto.getDeuda());
        ec.setIntereses(dto.getIntereses());
        ec.setTotalA(dto.getTotalA());
        ec.setTotalB(dto.getTotalB());
        ec.setTotalC(dto.getTotalC());
        ec.setTotalD(dto.getTotalD());
        ec.setTotalE(dto.getTotalE());
        ec.setGastoParticular(dto.getGastoParticular());
        ec.setRedondeo(dto.getRedondeo());
        ec.setTotalExpensa(dto.getTotalExpensa());
        ec.setSegundoVencimientoActivo(dto.getSegundoVencimientoActivo());
        ec.setSegundoVencimiento(dto.getSegundoVencimiento());
        ec.setSaldoFinal(dto.getSaldoFinal());
        ec.setSaldoExpensa(dto.getSaldoExpensa());
        ec.setSaldoIntereses(dto.getSaldoIntereses());

        return ec;
    }
    // MAP ENTITY TO DTO
    public EstadoCuentaUfDTO mapToEstadoCuentaUfDTO(EstadoCuentaUf ea){
        EstadoCuentaUfDTO dto = new EstadoCuentaUfDTO();

        dto.setIdEstadoCuentaUf(ea.getIdEstadoCuentaUf());
        dto.setIdUf(ea.getUnidadFuncional().getIdUf());
        dto.setPeriodo(ea.getPeriodo());
        dto.setTotalMesPrevio(ea.getTotalMesPrevio());
        dto.setDeuda(ea.getDeuda());
        dto.setIntereses(ea.getIntereses());
        dto.setTotalA(ea.getTotalA());
        dto.setTotalB(ea.getTotalB());
        dto.setTotalC(ea.getTotalC());
        dto.setTotalD(ea.getTotalD());
        dto.setTotalE(ea.getTotalE());
        dto.setGastoParticular(ea.getGastoParticular());
        dto.setRedondeo(ea.getRedondeo());
        dto.setTotalExpensa(ea.getTotalExpensa());
        dto.setSegundoVencimientoActivo(ea.getSegundoVencimientoActivo());
        dto.setSegundoVencimiento(ea.getSegundoVencimiento());
        dto.setSaldoFinal(ea.getSaldoFinal());
        dto.setSaldoExpensa(ea.getSaldoExpensa());
        dto.setSaldoIntereses(ea.getSaldoIntereses());


        return dto;
    }
    // CREAR COPIA DE ESTADO DE CUENTA
    public void createCopiaEstadoCuentaUf(EstadoCuentaUfDTO dto) throws Exception {
        if(estadoCuentaUfRepository.findById(dto.getIdEstadoCuentaUf()).isEmpty()){
            throw new Exception("Estado de cuenta no encontrado");
        }

        copiaEstadoCuentaUfService.createCopiaEstadoCuentaUf(dto);
        Optional<EstadoCuentaUf> estadoCuentaUf = estadoCuentaUfRepository.findById(dto.getIdEstadoCuentaUf());


        // LIMPIA VALORES
        estadoCuentaUf.get().setPeriodo(estadoCuentaUf.get().getPeriodo().plusMonths(1));
        estadoCuentaUf.get().setTotalMesPrevio(estadoCuentaUf.get().getTotalExpensa());
        estadoCuentaUf.get().setTotalA(0.0);
        estadoCuentaUf.get().setTotalB(0.0);
        estadoCuentaUf.get().setTotalC(0.0);
        estadoCuentaUf.get().setTotalD(0.0);
        estadoCuentaUf.get().setTotalE(0.0);
        estadoCuentaUf.get().setGastoParticular(0.0);
        estadoCuentaUf.get().setRedondeo(0.0);

        estadoCuentaUfRepository.save(estadoCuentaUf.get());
    }

    // CHEQUEO INTERESES
    private double verificarIntereses(EstadoCuentaUfDTO dto, PagoUF pago) {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(dto.getIdEstadoCuentaUf()).get();

        double saldoIntereses = estadoCuentaUf.getSaldoIntereses();
        double pagoTotal = pago.getValor();
        double diferencia;
        double interesesPagados;

        if (saldoIntereses > 0) {
            if (pagoTotal >= saldoIntereses) {
                diferencia = pagoTotal - saldoIntereses;
                interesesPagados = saldoIntereses;
                estadoCuentaUf.setSaldoInteresesCero();
            } else {
                diferencia = 0;
                interesesPagados = pagoTotal;
                estadoCuentaUf.setSaldoIntereses(saldoIntereses - pagoTotal);
            }
        } else {
            diferencia = pagoTotal;
            interesesPagados = 0;
        }

        pago.setInteresesPagos(interesesPagados);

        return diferencia;
    }

    // Pagos Uf
    public void restarPago (EstadoCuentaUfDTO dto, PagoUF pago){
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(dto.getIdEstadoCuentaUf()).get();
        double pagoTotal = pago.getValor();

        double diferenciaIntereses = verificarIntereses(dto, pago);

        estadoCuentaUf.setSaldoExpensa(dto.getSaldoExpensa() - diferenciaIntereses);

        estadoCuentaUf.setSaldoFinal(dto.getSaldoFinal() - pagoTotal);

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }

    public void revertirPago(EstadoCuentaUf estadoCuentaUf, PagoUF pago){
        double pagoTotal = pago.getValor();
        double intereses = pago.getInteresesPagos();
        double diferenciaTotalIntereses = pagoTotal - intereses;

        estadoCuentaUf.setSaldoFinal(estadoCuentaUf.getSaldoFinal() + pagoTotal);
        if(intereses == 0){
            estadoCuentaUf.setSaldoExpensa(estadoCuentaUf.getSaldoExpensa() + pagoTotal);
        } else{
            estadoCuentaUf.setSaldoExpensa(estadoCuentaUf.getSaldoExpensa() + diferenciaTotalIntereses);
            estadoCuentaUf.setSaldoIntereses(estadoCuentaUf.getSaldoIntereses() + intereses);
        }

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }


    public List<EstadoCuentaUfDTO> createEstadosDeCuentaAuxiliares(List<EstadoCuentaUfDTO> estadosDeCuentaUf) {
        List<EstadoCuentaUfDTO> estadoCuentaUfAuxiliaresDTOS = new ArrayList<>();
        for(EstadoCuentaUfDTO ea : estadosDeCuentaUf){
            EstadoCuentaUfDTO ecAux = new EstadoCuentaUfDTO();
            copiarEstadoCuentaAux(ea, ecAux);
            estadoCuentaUfAuxiliaresDTOS.add(ecAux);
        }

        return estadoCuentaUfAuxiliaresDTOS;
    }

    private void copiarEstadoCuentaAux(EstadoCuentaUfDTO ea, EstadoCuentaUfDTO ecAux) {
        ecAux.setIdEstadoCuentaUf(ea.getIdEstadoCuentaUf());
        ecAux.setIdUf(ea.getIdUf());
        ecAux.setPeriodo(ea.getPeriodo());
        ecAux.setTotalMesPrevio(ea.getTotalMesPrevio());
        ecAux.setDeuda(ea.getDeuda());
        ecAux.setIntereses(ea.getIntereses());
        ecAux.setTotalA(0.0);
        ecAux.setTotalB(0.0);
        ecAux.setTotalC(0.0);
        ecAux.setTotalD(0.0);
        ecAux.setTotalE(0.0);
        ecAux.setGastoParticular(0.0);
        ecAux.setRedondeo(0.0);
        ecAux.setTotalExpensa(ea.getTotalExpensa());
        ecAux.setSaldoFinal(ea.getSaldoFinal());
        ecAux.setSaldoExpensa(ea.getSaldoExpensa());
        ecAux.setSaldoIntereses(ea.getSaldoIntereses());
    }


}
