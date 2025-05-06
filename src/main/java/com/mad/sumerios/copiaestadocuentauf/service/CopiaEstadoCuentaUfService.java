package com.mad.sumerios.copiaestadocuentauf.service;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.copiaestadocuentauf.dto.CopiaEstadoCuentaUfDTO;
import com.mad.sumerios.copiaestadocuentauf.model.CopiaEstadoCuentaUf;
import com.mad.sumerios.copiaestadocuentauf.repository.IEstadoCuentaUfCopiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
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
    public void createCopiaEstadoCuentaUf (EstadoCuentaUfDTO dto) throws Exception{
        CopiaEstadoCuentaUf copia = mapToCopiaEstadoCuentaUfEntity(dto);
        estadoCuentaUfCopiaRepository.save(copia);
    }

    public List<CopiaEstadoCuentaUfDTO> getCopiasEstadoCuentaUf(Long idUf) {
        List<CopiaEstadoCuentaUf> copias = estadoCuentaUfCopiaRepository.findByIdUf(idUf);
        return copias.stream().map(this::mapToCopiaEstadoCuentaUfDTO).collect(Collectors.toList());
    }

    public CopiaEstadoCuentaUfDTO getCopiasEstadoCuentaUfByIdEstadoCuentaUfAndPeriodo(Long idEstadoCuentaUf, YearMonth periodo) {
        return mapToCopiaEstadoCuentaUfDTO(estadoCuentaUfCopiaRepository.findByIdEstadoCuentaUfAndPeriodo(idEstadoCuentaUf, periodo));
    }

    public void deleteCopiaEstadoCuentaUf(Long idCopiaEstadoCuentaUf){
        estadoCuentaUfCopiaRepository.deleteById(idCopiaEstadoCuentaUf);
    }


    // MAP TO ENTITY
    private CopiaEstadoCuentaUf mapToCopiaEstadoCuentaUfEntity(EstadoCuentaUfDTO dto) throws Exception {


        CopiaEstadoCuentaUf copiaEstadoCuentaUf = new CopiaEstadoCuentaUf();

        copiaEstadoCuentaUf.setIdEstadoCuentaUf(dto.getIdEstadoCuentaUf());
        copiaEstadoCuentaUf.setIdUf(dto.getIdUf());
        copiaEstadoCuentaUf.setPeriodo(dto.getPeriodo());
        if(dto.getTotalMesPrevio() == null) {
            copiaEstadoCuentaUf.setTotalMesPrevio(0.0);
        } else{
            copiaEstadoCuentaUf.setTotalMesPrevio(dto.getTotalMesPrevio());
        }
        copiaEstadoCuentaUf.setDeuda(dto.getDeuda());
        copiaEstadoCuentaUf.setIntereses(dto.getIntereses());
        copiaEstadoCuentaUf.setTotalA(dto.getTotalA());
        copiaEstadoCuentaUf.setTotalB(dto.getTotalB());
        copiaEstadoCuentaUf.setTotalC(dto.getTotalC());
        copiaEstadoCuentaUf.setTotalD(dto.getTotalD());
        copiaEstadoCuentaUf.setTotalE(dto.getTotalE());
        copiaEstadoCuentaUf.setGastoParticular(dto.getGastoParticular());

        if(dto.getRedondeo() == null) {
            copiaEstadoCuentaUf.setRedondeo(0.0);
        } else{
            copiaEstadoCuentaUf.setRedondeo(dto.getTotalMesPrevio());
        }

        if(dto.getTotalExpensa() != null){
            copiaEstadoCuentaUf.setTotalExpensa(dto.getTotalExpensa());
        } else {
            copiaEstadoCuentaUf.setTotalExpensa(dto.getDeuda()+
                    dto.getIntereses()+
                    dto.getTotalA()+
                    dto.getTotalB()+
                    dto.getTotalC()+
                    dto.getTotalD()+
                    dto.getTotalE()+
                    dto.getGastoParticular());
        }

        if(dto.getSegundoVencimientoActivo() != null) {
            copiaEstadoCuentaUf.setSegundoVencimientoActivo(dto.getSegundoVencimientoActivo());
        } else {
            copiaEstadoCuentaUf.setSegundoVencimientoActivo(false);
        }

        if(dto.getSegundoVencimiento() != null) {
            copiaEstadoCuentaUf.setSegundoVencimiento(dto.getSegundoVencimiento());
        } else {
            copiaEstadoCuentaUf.setSegundoVencimiento(0.0);
        }

        copiaEstadoCuentaUf.setSaldoFinal(dto.getSaldoFinal());
        copiaEstadoCuentaUf.setSaldoExpensa(dto.getSaldoExpensa());
        copiaEstadoCuentaUf.setSaldoIntereses(dto.getSaldoIntereses());

        return copiaEstadoCuentaUf;
    }

    // MAP ENTITY TO DTO
    private CopiaEstadoCuentaUfDTO mapToCopiaEstadoCuentaUfDTO(CopiaEstadoCuentaUf copiaEstadoCuentaUf) {
        CopiaEstadoCuentaUfDTO dto = new CopiaEstadoCuentaUfDTO();

        dto.setIdCopiaEstadoCuentaUf(copiaEstadoCuentaUf.getIdCopiaEstadoCuentaUf());
        dto.setIdEstadoCuentaUf(copiaEstadoCuentaUf.getIdEstadoCuentaUf());
        dto.setIdUf(copiaEstadoCuentaUf.getIdUf());
        dto.setPeriodo(copiaEstadoCuentaUf.getPeriodo());
        if(copiaEstadoCuentaUf.getTotalMesPrevio() != null){
            dto.setTotalMesPrevio(copiaEstadoCuentaUf.getTotalMesPrevio());
        } else {
            dto.setTotalMesPrevio(0.0);
        }
        dto.setDeuda(copiaEstadoCuentaUf.getDeuda());
        dto.setIntereses(copiaEstadoCuentaUf.getIntereses());
        dto.setTotalA(copiaEstadoCuentaUf.getTotalA());
        dto.setTotalB(copiaEstadoCuentaUf.getTotalB());
        dto.setTotalC(copiaEstadoCuentaUf.getTotalB());
        dto.setTotalD(copiaEstadoCuentaUf.getTotalC());
        dto.setTotalE(copiaEstadoCuentaUf.getTotalD());
        dto.setGastoParticular(copiaEstadoCuentaUf.getGastoParticular());
        if(copiaEstadoCuentaUf.getRedondeo() == null) {
            dto.setRedondeo(0.0);
        } else{
            dto.setRedondeo(copiaEstadoCuentaUf.getRedondeo());
        }
        dto.setTotalExpensa(copiaEstadoCuentaUf.getTotalExpensa());
        if(copiaEstadoCuentaUf.getSegundoVencimientoActivo() == null) {
            dto.setSegundoVencimientoActivo(false);
        } else{
            dto.setSegundoVencimientoActivo(copiaEstadoCuentaUf.getSegundoVencimientoActivo());
        }
        if(copiaEstadoCuentaUf.getSegundoVencimiento() == null) {
            dto.setSegundoVencimiento(0.0);
        } else{
            dto.setSegundoVencimiento(copiaEstadoCuentaUf.getSegundoVencimiento());
        }
        dto.setSaldoFinal(copiaEstadoCuentaUf.getSaldoFinal());
        dto.setSaldoExpensa(copiaEstadoCuentaUf.getSaldoExpensa());
        dto.setSaldoIntereses(copiaEstadoCuentaUf.getSaldoIntereses());

        return dto;
    }

}
