package com.mad.sumerios.ingreso.service;

import com.mad.sumerios.ingreso.model.Ingreso;
import com.mad.sumerios.ingreso.repository.IIngresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class IngresoService {

    private final IIngresoRepository ingresoRepository;

    @Autowired
    public IngresoService(IIngresoRepository ingresoRepository) {
        this.ingresoRepository  = ingresoRepository;
    }

    //  CREAR INGRESO
    public void createIngreso (Ingreso ingreso) throws Exception{
        if(ingreso == null){
            throw new Exception("Ingreso nulo");
        }
        ingresoRepository.save(ingreso);
    }

    //  LISTAR INGRESO
    //  POR UNIDAD FUNCIONAL
    public List<Ingreso> getIngresoPorUnidadFuncional(Long uf_id){
        return ingresoRepository.findByUnidadFuncional_id(uf_id);
    }

    //  POR CONSORCIO Y FECHA
    public List<Ingreso> getIngresoPorConsorcioYFecha(Long consorcio_id, Date fechaInicio, Date fechaFin){
        return ingresoRepository.findByConsorcio_IdConsorcioAndFechaBetween(consorcio_id, fechaInicio, fechaFin);
    }

    //  ACTUALIZAR INGRESO
    public void updateIngreso(Ingreso ingreso) throws Exception{
        Ingreso ing = ingresoRepository.findById(ingreso.getIdIngreso())
                .orElseThrow(()-> new Exception("Ingreso no encontrado"));

        ing.setFecha(ingreso.getFecha());
        ing.setValor(ingreso.getValor());
        ing.setDescripcion(ingreso.getDescripcion());
        ing.setFormaPago(ingreso.getFormaPago());
    }

    //  ELIMINAR INGRESO
    public void deleteIngreso(Long id) throws Exception{
        Ingreso ing = ingresoRepository.findById(id)
                .orElseThrow(()-> new Exception("Ingreso no encontrado"));

        ingresoRepository.delete(ing);
    }
}
