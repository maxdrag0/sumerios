package com.mad.sumerios.estadocuentauf.service;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfCreateDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.estadocuentauf.repository.IEstadoCuentaUfRepository;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.repository.IUnidadFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstadoCuentaUfService {

    private final IEstadoCuentaUfRepository estadoCuentaUfRepository;
    private final IUnidadFuncionalRepository unidadFuncionalRepository;

    @Autowired
    public EstadoCuentaUfService (IEstadoCuentaUfRepository estadoCuentaUfRepository,
                                  IUnidadFuncionalRepository unidadFuncionalRepository){
        this.estadoCuentaUfRepository = estadoCuentaUfRepository;
        this.unidadFuncionalRepository = unidadFuncionalRepository;
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
    //  BY UF
    public EstadoCuentaUfDTO getEstadoCuentaUf (Long idUf){
        return mapToEstadoCuentaUfDTO(estadoCuentaUfRepository.findByUnidadFuncional_idUf(idUf));
    }

    // VALIDACIONES Y AUX
    private void validateValor(Double deuda,
                               Double intereses,
                               Double totalA,
                               Double totalB,
                               Double totalC,
                               Double totalD,
                               Double totalE,
                               Double gastoParticular,
                               Double totalFinal) throws Exception {
        double suma = deuda+intereses+totalA+totalB+totalC+totalD+totalE+gastoParticular;
        if(suma != totalFinal) {
            throw new Exception(
                    "El reparto valores es de $"+ suma +" mientras que el total es de $"+ totalFinal);
        }
    }
    private UnidadFuncional validateUf(Long idUf) throws Exception {
        Optional<UnidadFuncional> uf = unidadFuncionalRepository.findById(idUf);
        if(uf.isEmpty()){
            throw new Exception("Unidad funcional no encontrada");
        }

        return uf.get();
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
        validateValor(dto.getDeuda(),
                dto.getIntereses(),
                dto.getTotalA(),
                dto.getTotalB(),
                dto.getTotalC(),
                dto.getTotalD(),
                dto.getTotalE(),
                dto.getGastoParticular(),
                dto.getTotalFinal());

        if(unidadFuncionalRepository.findById(dto.getIdUf()).isEmpty()){
            throw new Exception("Unidad funcional no encontrada");

        }

        EstadoCuentaUf ec = new EstadoCuentaUf();

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
    private EstadoCuentaUfDTO mapToEstadoCuentaUfDTO(EstadoCuentaUf ea){
        EstadoCuentaUfDTO dto = new EstadoCuentaUfDTO();

        dto.setIdUf(ea.getUnidadFuncional().getIdUf());
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


    // CHEQUEO INTERESES
    private double verificarIntereses(EstadoCuentaUf estadoCuentaUf, PagoUF pago) {
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
    public void restarPago (EstadoCuentaUf estadoCuentaUf, PagoUF pago){
        double pagoTotal = pago.getValor();
        double diferenciaIntereses = verificarIntereses(estadoCuentaUf, pago);

        estadoCuentaUf.setSaldoExpensa(estadoCuentaUf.getSaldoExpensa() - diferenciaIntereses);
        estadoCuentaUf.setTotalFinal(estadoCuentaUf.getTotalFinal() - pagoTotal);

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
