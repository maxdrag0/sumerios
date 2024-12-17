package com.mad.sumerios.copiaestadocuentauf.service;

import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.copiaestadocuentauf.dto.CopiaEstadoCuentaUfDTO;
import com.mad.sumerios.copiaestadocuentauf.model.CopiaEstadoCuentaUf;
import com.mad.sumerios.copiaestadocuentauf.repository.IEstadoCuentaUfCopiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CopiaEstadoCuentaUfService {

    private final IEstadoCuentaUfCopiaRepository estadoCuentaUfCopiaRepository;

    @Autowired
    public CopiaEstadoCuentaUfService(IEstadoCuentaUfCopiaRepository estadoCuentaUfCopiaRepository){
        this.estadoCuentaUfCopiaRepository = estadoCuentaUfCopiaRepository;
    }

    //  CREATE ESTADO CUENTA
    public void createCopiaEstadoCuentaUf (EstadoCuentaUf estadoCuentaUf) throws Exception{
        estadoCuentaUfCopiaRepository.save(mapToCopiaEstadoCuentaUfEntity(estadoCuentaUf));
    }

    public List<CopiaEstadoCuentaUfDTO> getCopiasEstadoCuentaUf(Long idUf) {
        List<CopiaEstadoCuentaUf> copias = estadoCuentaUfCopiaRepository.findByIdUf(idUf);
        return copias.stream().map(this::mapToCopiaEstadoCuentaUfDTO).collect(Collectors.toList());
    }



    // MAP TO ENTITY
    private CopiaEstadoCuentaUf mapToCopiaEstadoCuentaUfEntity(EstadoCuentaUf estadoCuentaUf) throws Exception {


        CopiaEstadoCuentaUf copiaEstadoCuentaUf = new CopiaEstadoCuentaUf();

        copiaEstadoCuentaUf.setIdEstadoCuentaUf(estadoCuentaUf.getIdEstadoCuentaUf());
        copiaEstadoCuentaUf.setIdUf(estadoCuentaUf.getUnidadFuncional().getIdUf());
        copiaEstadoCuentaUf.setPeriodo(estadoCuentaUf.getPeriodo());
        copiaEstadoCuentaUf.setDeuda(estadoCuentaUf.getDeuda());
        copiaEstadoCuentaUf.setIntereses(estadoCuentaUf.getIntereses());
        copiaEstadoCuentaUf.setTotalA(estadoCuentaUf.getTotalA());
        copiaEstadoCuentaUf.setTotalB(estadoCuentaUf.getTotalB());
        copiaEstadoCuentaUf.setTotalC(estadoCuentaUf.getTotalB());
        copiaEstadoCuentaUf.setTotalD(estadoCuentaUf.getTotalC());
        copiaEstadoCuentaUf.setTotalE(estadoCuentaUf.getTotalD());
        copiaEstadoCuentaUf.setGastoParticular(estadoCuentaUf.getGastoParticular());
        copiaEstadoCuentaUf.setTotalFinal(estadoCuentaUf.getTotalFinal());

        return copiaEstadoCuentaUf;
    }

    // MAP ENTITY TO DTO
    private CopiaEstadoCuentaUfDTO mapToCopiaEstadoCuentaUfDTO(CopiaEstadoCuentaUf copiaEstadoCuentaUf) {
        CopiaEstadoCuentaUfDTO dto = new CopiaEstadoCuentaUfDTO();

        dto.setIdCopiaEstadoCuentaUf(copiaEstadoCuentaUf.getIdCopiaEstadoCuentaUf());
        dto.setIdEstadoCuentaUf(copiaEstadoCuentaUf.getIdEstadoCuentaUf());
        dto.setIdUf(copiaEstadoCuentaUf.getIdUf());
        dto.setPeriodo(copiaEstadoCuentaUf.getPeriodo());
        dto.setDeuda(copiaEstadoCuentaUf.getDeuda());
        dto.setIntereses(copiaEstadoCuentaUf.getIntereses());
        dto.setTotalA(copiaEstadoCuentaUf.getTotalA());
        dto.setTotalB(copiaEstadoCuentaUf.getTotalB());
        dto.setTotalC(copiaEstadoCuentaUf.getTotalB());
        dto.setTotalD(copiaEstadoCuentaUf.getTotalC());
        dto.setTotalE(copiaEstadoCuentaUf.getTotalD());
        dto.setGastoParticular(copiaEstadoCuentaUf.getGastoParticular());
        dto.setTotalFinal(copiaEstadoCuentaUf.getTotalFinal());

        return dto;
    }

}
