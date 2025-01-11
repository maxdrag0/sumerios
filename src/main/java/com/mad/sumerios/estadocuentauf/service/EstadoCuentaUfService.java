package com.mad.sumerios.estadocuentauf.service;

import com.mad.sumerios.copiaestadocuentauf.dto.CopiaEstadoCuentaUfDTO;
import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfCreateDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
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
import java.util.stream.Collectors;

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
        ec.setDeuda(ecUpdated.getDeuda());
        ec.setIntereses(ecUpdated.getIntereses());
        ec.setTotalA(ecUpdated.getTotalA());
        ec.setTotalB(ecUpdated.getTotalB());
        ec.setTotalC(ecUpdated.getTotalC());
        ec.setTotalD(ecUpdated.getTotalD());
        ec.setTotalE(ecUpdated.getTotalE());
        ec.setGastoParticular(ecUpdated.getGastoParticular());
        ec.setTotalFinal(ecUpdated.getTotalFinal());
        ec.setSaldoExpensa(dto.getSaldoExpensa());
        ec.setSaldoIntereses(dto.getSaldoIntereses());

        estadoCuentaUfRepository.save(ec);
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
        System.out.println("3 Dentro de restablecer periodo de ECUF: ");

        EstadoCuentaUfDTO estadoActual = this.getEstadoCuentaUfById(idEstadoCuentaUf);
        System.out.println("4 ECUF Periodo de estado actual: "+ estadoActual.getPeriodo());
        System.out.println("5 ECUF Periodo de estado viejo: "+ estadoActual.getPeriodo().minusMonths(1));
        CopiaEstadoCuentaUfDTO copiaEstadoPeriodoPrevio = copiaEstadoCuentaUfService.getCopiasEstadoCuentaUfByIdEstadoCuentaUfAndPeriodo(
                idEstadoCuentaUf,
                estadoActual.getPeriodo().minusMonths(1));
        if(copiaEstadoPeriodoPrevio == null){
            throw new Exception("Copia no encontrada");
        }
        System.out.println("6 COPIA ECUF Periodo de estado VIEJO: "+ copiaEstadoPeriodoPrevio.getPeriodo());


        // COPIO LOS DATOS VIEJOS AL ESTADO ACTUAL
        System.out.println("7 ECUF ANTES DE ACTUALIZAR: "+ estadoActual);

        estadoActual.setPeriodo(copiaEstadoPeriodoPrevio.getPeriodo());
        estadoActual.setDeuda(copiaEstadoPeriodoPrevio.getDeuda());
        estadoActual.setIntereses(copiaEstadoPeriodoPrevio.getIntereses());
        estadoActual.setTotalA(copiaEstadoPeriodoPrevio.getTotalA());
        estadoActual.setTotalB(copiaEstadoPeriodoPrevio.getTotalB());
        estadoActual.setTotalC(copiaEstadoPeriodoPrevio.getTotalC());
        estadoActual.setTotalD(copiaEstadoPeriodoPrevio.getTotalD());
        estadoActual.setTotalE(copiaEstadoPeriodoPrevio.getTotalE());
        estadoActual.setGastoParticular(copiaEstadoPeriodoPrevio.getGastoParticular());
        estadoActual.setTotalFinal(copiaEstadoPeriodoPrevio.getTotalFinal());
        estadoActual.setSaldoExpensa(copiaEstadoPeriodoPrevio.getSaldoExpensa());
        estadoActual.setSaldoIntereses(copiaEstadoPeriodoPrevio.getSaldoIntereses());
        System.out.println("8 ECUF DESPUES DE ACTUALIZAR: "+ estadoActual);

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
    public void aplicarValorCategoria(long idEstadoCuentaUf,
                             double porcentajeUf,
                             double total,
                             CategoriaEgreso categoria) throws Exception {
        EstadoCuentaUfDTO dto = this.getEstadoCuentaUfById(idEstadoCuentaUf);

        double totalAAplicar = (total > 0) ? (total * porcentajeUf) / 100 : 0;

        if (totalAAplicar>0){
            EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                    .orElseThrow(()-> new Exception("Estado de cuenta no encotnrado."));
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
    public void aplicarDeudaEIntereses(long idEstadoCuentaUf, double porcentajeIntereses) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(idEstadoCuentaUf)
                .orElseThrow(()->new Exception("Estado de cuenta no encontrado"));


        double deuda = estadoCuentaUf.getTotalFinal() - estadoCuentaUf.getSaldoIntereses();
        double intereses = (deuda*porcentajeIntereses)/100;

        estadoCuentaUf.setDeuda(deuda);
        estadoCuentaUf.setIntereses(intereses);
        // Modifico saldo expensa e saldo intereses
        estadoCuentaUf.setTotalFinal(0.0);
        estadoCuentaUf.setSaldoExpensa(0.0);
        estadoCuentaUf.setSaldoIntereses(intereses);

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

        estadoCuentaUf.setSaldoExpensa(valorDeExpensa + estadoCuentaUf.getDeuda());

        estadoCuentaUf.setTotalFinal(valorDeExpensa+valorDeudaEIntereses);

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }
    // MAP DTO TO ENTITY
    private EstadoCuentaUf mapToEstadoCuentaUfEntity(EstadoCuentaUfCreateDTO dto) throws Exception {
        UnidadFuncional uf = validateUf(dto.getIdUf());

        if(uf.getEstadoCuentaUf() != null){
            throw new Exception("La unidad funcional ya tiene un estado de cuenta asociado.");
        }

        EstadoCuentaUf ec = new EstadoCuentaUf();

        ec.setUnidadFuncional(uf);
        ec.setDeuda(0.0);
        ec.setIntereses(0.0);
        ec.setTotalA(0.0);
        ec.setTotalB(0.0);
        ec.setTotalC(0.0);
        ec.setTotalD(0.0);
        ec.setTotalE(0.0);
        ec.setGastoParticular(0.0);
        ec.setTotalFinal(0.0);
        ec.setSaldoExpensa(0.0);
        ec.setSaldoIntereses(0.0);

        uf.setEstadoCuentaUf(ec);

        return ec;
    }
    private EstadoCuentaUf mapToEstadoCuentaUfEntityUpdate(EstadoCuentaUfDTO dto) throws Exception {
//        validateValor(dto.getDeuda(),
//                dto.getIntereses(),
//                dto.getTotalA(),
//                dto.getTotalB(),
//                dto.getTotalC(),
//                dto.getTotalD(),
//                dto.getTotalE(),
//                dto.getGastoParticular(),
//                dto.getTotalFinal());

        if(unidadFuncionalRepository.findById(dto.getIdUf()).isEmpty()){
            throw new Exception("Unidad funcional no encontrada");

        }

        EstadoCuentaUf ec = new EstadoCuentaUf();


        ec.setPeriodo(dto.getPeriodo());
        ec.setDeuda(dto.getDeuda());
        ec.setIntereses(dto.getIntereses());
        ec.setTotalA(dto.getTotalA());
        ec.setTotalB(dto.getTotalB());
        ec.setTotalC(dto.getTotalC());
        ec.setTotalD(dto.getTotalD());
        ec.setTotalE(dto.getTotalE());
        ec.setGastoParticular(dto.getGastoParticular());
        ec.setTotalFinal(dto.getTotalFinal());
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
        dto.setDeuda(ea.getDeuda());
        dto.setTotalA(ea.getTotalA());
        dto.setTotalB(ea.getTotalB());
        dto.setTotalC(ea.getTotalC());
        dto.setTotalD(ea.getTotalD());
        dto.setTotalE(ea.getTotalE());
        dto.setGastoParticular(ea.getGastoParticular());
        dto.setTotalFinal(ea.getTotalFinal());
        dto.setSaldoExpensa(ea.getSaldoExpensa());
        dto.setSaldoIntereses(ea.getSaldoIntereses());


        return dto;
    }
    // CREAR COPIA DE ESTADO DE CUENTA
    public void createCopiaEstadoCuentaUf(EstadoCuentaUfDTO dto) throws Exception {
        EstadoCuentaUf estadoCuentaUf = estadoCuentaUfRepository.findById(dto.getIdEstadoCuentaUf())
                .orElseThrow(()-> new Exception("Estado de cuenta no encontrado"));
        copiaEstadoCuentaUfService.createCopiaEstadoCuentaUf(estadoCuentaUf);

        // LIMPIA VALORES
        estadoCuentaUf.setPeriodo(estadoCuentaUf.getPeriodo().plusMonths(1));
        estadoCuentaUf.setTotalA(0.0);
        estadoCuentaUf.setTotalB(0.0);
        estadoCuentaUf.setTotalC(0.0);
        estadoCuentaUf.setTotalD(0.0);
        estadoCuentaUf.setTotalE(0.0);
        estadoCuentaUf.setGastoParticular(0.0);

        estadoCuentaUfRepository.save(estadoCuentaUf);
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

        estadoCuentaUf.setTotalFinal(dto.getTotalFinal() - pagoTotal);

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }

    public void revertirPago(EstadoCuentaUf estadoCuentaUf, PagoUF pago){
        double pagoTotal = pago.getValor();
        double intereses = pago.getInteresesPagos();
        double diferenciaTotalIntereses = pagoTotal - intereses;

        estadoCuentaUf.setTotalFinal(estadoCuentaUf.getTotalFinal() + pagoTotal);
        if(intereses == 0){
            estadoCuentaUf.setSaldoExpensa(estadoCuentaUf.getSaldoExpensa() + pagoTotal);
        } else{
            estadoCuentaUf.setSaldoExpensa(estadoCuentaUf.getSaldoExpensa() + diferenciaTotalIntereses);
            estadoCuentaUf.setSaldoIntereses(estadoCuentaUf.getSaldoIntereses() + intereses);
        }

        estadoCuentaUfRepository.save(estadoCuentaUf);
    }


}
