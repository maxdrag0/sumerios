package com.mad.sumerios.copiaestadocuentaconsorcio.service;

import com.mad.sumerios.copiaestadocuentaconsorcio.dto.CopiaEstadoCuentaConsorcioDTO;
import com.mad.sumerios.copiaestadocuentaconsorcio.model.CopiaEstadoCuentaConsorcio;
import com.mad.sumerios.copiaestadocuentaconsorcio.repository.ICopiaEstadoCuentaConsorcioRepository;
import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
public class CopiaEstadoCuentaConsorcioService {

    private final ICopiaEstadoCuentaConsorcioRepository copiaEstadoCuentaConsorcioRepository;

    @Autowired
    public CopiaEstadoCuentaConsorcioService (ICopiaEstadoCuentaConsorcioRepository copiaEstadoCuentaConsorcioRepository){
        this.copiaEstadoCuentaConsorcioRepository = copiaEstadoCuentaConsorcioRepository;
    }

    // CREATE COPIA
    public void createCopiaEstadoCuentaConsorcio (EstadoCuentaConsorcio estadoCuentaConsorcio, YearMonth periodo) throws Exception{
        copiaEstadoCuentaConsorcioRepository.save(mapToCopiaEstadoCuentaConsorcioEntity(estadoCuentaConsorcio, periodo));
    }

    // GET BY CONSORCIO AND PERIODO
    public CopiaEstadoCuentaConsorcioDTO getByConsorcioAndPeriodo(Long idConsorcio, YearMonth periodo){
        CopiaEstadoCuentaConsorcio copia = copiaEstadoCuentaConsorcioRepository.findByIdConsorcioAndPeriodo(idConsorcio, periodo);
        return mapToCopiaEstadoCuentaConsorcioDto(copia);
    }

    // MAPEOS
    private CopiaEstadoCuentaConsorcioDTO mapToCopiaEstadoCuentaConsorcioDto(CopiaEstadoCuentaConsorcio copia) {
        CopiaEstadoCuentaConsorcioDTO dto = new CopiaEstadoCuentaConsorcioDTO();

        dto.setIdCopiaEstadoCuentaConsorcio(copia.getIdCopiaEstadoCuentaConsorcio());
        dto.setIdConsorcio(copia.getIdConsorcio());
        dto.setPeriodo(copia.getPeriodo());
        dto.setEfectivo(copia.getEfectivo());
        dto.setBanco(copia.getBanco());
        dto.setFondoAdm(copia.getFondoAdm());
        dto.setTotal(copia.getTotal());

        return dto;
    }

    private CopiaEstadoCuentaConsorcio mapToCopiaEstadoCuentaConsorcioEntity(EstadoCuentaConsorcio estadoCuentaConsorcio,
                                                                             YearMonth periodo) {
        CopiaEstadoCuentaConsorcio copiaEstadoCuentaConsorcio = new CopiaEstadoCuentaConsorcio();

        copiaEstadoCuentaConsorcio.setIdConsorcio(estadoCuentaConsorcio.getConsorcio().getIdConsorcio());
        copiaEstadoCuentaConsorcio.setPeriodo(periodo.minusMonths(1));
        copiaEstadoCuentaConsorcio.setEfectivo(estadoCuentaConsorcio.getEfectivo());
        copiaEstadoCuentaConsorcio.setBanco(estadoCuentaConsorcio.getBanco());
        copiaEstadoCuentaConsorcio.setFondoAdm(estadoCuentaConsorcio.getFondoAdm());
        copiaEstadoCuentaConsorcio.setTotal(estadoCuentaConsorcio.getTotal());

        return copiaEstadoCuentaConsorcio;
    }


}
