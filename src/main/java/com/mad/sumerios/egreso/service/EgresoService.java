package com.mad.sumerios.egreso.service;

import com.mad.sumerios.egreso.model.Egreso;
import com.mad.sumerios.egreso.repository.IEgresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EgresoService {

    private final IEgresoRepository egresoRepository;

    @Autowired
    public EgresoService(IEgresoRepository egresoRepository) {
        this.egresoRepository = egresoRepository;
    }

    //  CREAR EGRESO
    public void createEgreso(Egreso egreso) throws Exception{
        if(egreso == null){
            throw new Exception("Egreso nulo");
        }
        egresoRepository.save(egreso);
    }

    //  LISTAR EGRESOS CONSORCIO Y FECHA
    public List<Egreso> getEgresosPorConsorcioYFechas(Long idConsorcio, Date fechaInicio, Date fechaFin) {
        return egresoRepository.findByConsorcio_IdConsorcioAndFechaBetween(idConsorcio, fechaInicio, fechaFin);
    }

    //  ACTUALIZAR CONSORCIO
    public void updateEgreso (Egreso egreso) throws Exception{
        Egreso eg = egresoRepository.findById(egreso.getIdMovimientoEgreso())
                .orElseThrow(()-> new Exception("Egreso no encontrado"));

        eg.setFecha(egreso.getFecha());
        eg.setTipoEgreso(egreso.getTipoEgreso());
        eg.setTitulo(egreso.getTitulo());
        eg.setDescripcion(egreso.getDescripcion());
        eg.setTotalA(eg.getTotalA());
        eg.setTotalB(eg.getTotalB());
        eg.setTotalC(eg.getTotalC());
        eg.setTotalD(eg.getTotalD());
        eg.setTotalE(eg.getTotalE());

        egresoRepository.save(eg);
    }

    //  ELIMINAR EGRESO
    public void deleteEgreso(Long id) throws Exception{
        Egreso eg = egresoRepository.findById(id)
                .orElseThrow(()-> new Exception("Egreso no encontrado"));

        egresoRepository.delete(eg);
    }
}
