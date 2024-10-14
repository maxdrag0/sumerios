package com.mad.sumerios.pendientes.movimientos.pagouf.service;

import com.mad.sumerios.pendientes.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.pendientes.movimientos.pagouf.repository.IPagoUFRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoUFService {

    private final IPagoUFRepository pagoUFRepository;

    @Autowired
    public PagoUFService(IPagoUFRepository ingresoRepository) {
        this.pagoUFRepository  = ingresoRepository;
    }

    //  CREAR INGRESO
    public void createPagoUF (PagoUF pagoUF) throws Exception{
        if(pagoUF == null){
            throw new Exception("Pago UF nulo");
        }
        pagoUFRepository.save(pagoUF);
    }

    //  LISTAR INGRESO
    //  POR UNIDAD FUNCIONAL
//    public List<PagoUF> getPagoUFPorUnidadFuncional(Long idUf){
//        return pagoUFRepository.findByUnidadFuncional_idUf(idUf);
//    }

//    //  POR CONSORCIO Y FECHA
//    public List<PagoUF> getPagoUFPorConsorcioYFecha(Long consorcio_id, Date fechaInicio, Date fechaFin){
//        return pagoUFRepository.findByConsorcio_IdConsorcioAndFechaBetween(consorcio_id, fechaInicio, fechaFin);
//    }

    //  ACTUALIZAR INGRESO
    public void updatePagoUF(PagoUF pagoUF) throws Exception{
        PagoUF ing = pagoUFRepository.findById(pagoUF.getIdPagoUF())
                .orElseThrow(()-> new Exception("Pago UF no encontrado"));

        ing.setFecha(pagoUF.getFecha());
        ing.setValor(pagoUF.getValor());
        ing.setDescripcion(pagoUF.getDescripcion());
        ing.setFormaPago(pagoUF.getFormaPago());
    }

    //  ELIMINAR INGRESO
    public void deletePagoUF(Long id) throws Exception{
        PagoUF ing = pagoUFRepository.findById(id)
                .orElseThrow(()-> new Exception("Pago UF no encontrado"));

        pagoUFRepository.delete(ing);
    }
}
