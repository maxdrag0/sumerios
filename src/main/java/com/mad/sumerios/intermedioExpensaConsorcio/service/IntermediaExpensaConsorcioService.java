package com.mad.sumerios.intermedioExpensaConsorcio.service;

import com.mad.sumerios.expensa.repository.IExpensaRepository;
import com.mad.sumerios.intermedioExpensaConsorcio.dto.IntermediaExpensaConsorcioCreateDto;
import com.mad.sumerios.intermedioExpensaConsorcio.dto.IntermediaExpensaConsorcioDto;
import com.mad.sumerios.intermedioExpensaConsorcio.model.IntermediaExpensaConsorcio;
import com.mad.sumerios.intermedioExpensaConsorcio.repository.IIntermediaExpensaConsorcioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntermediaExpensaConsorcioService {

    private final IIntermediaExpensaConsorcioRepository iIntermediaExpensaConsorcioRepository;
    private final IExpensaRepository expensaRepository;
    @Autowired
    public IntermediaExpensaConsorcioService (IIntermediaExpensaConsorcioRepository iIntermediaExpensaConsorcioRepository,
                                              IExpensaRepository expensaRepository){
        this.iIntermediaExpensaConsorcioRepository = iIntermediaExpensaConsorcioRepository;
        this.expensaRepository = expensaRepository;
    }


    public void createIntermediaExpensaConsorcio(IntermediaExpensaConsorcioCreateDto dto) throws Exception{
        IntermediaExpensaConsorcio intermedia = mapToIntermediaExpensaConsorcioEntity(dto);
        iIntermediaExpensaConsorcioRepository.save(intermedia);
    }

    public void updateIntermediaExpensaConsorcio(IntermediaExpensaConsorcioDto dto) throws Exception{
        IntermediaExpensaConsorcio intermedia = iIntermediaExpensaConsorcioRepository.findById(dto.getIdIntermedia())
                .orElseThrow(() -> new Exception("Intermedia no encontrada"));
        if(!intermedia.getIdConsorcio().equals(dto.getIdConsorcio())){
            throw new Exception("La clase intermedia no se corresponde con el consorcio.");
        } else if(expensaRepository.findById(dto.getIdExpensa()).isEmpty()){
            throw new Exception("No se encontró la expensa");
        }
        mapToIntermediaExpensaConsorcioUpdate(intermedia, dto);
    }

    public IntermediaExpensaConsorcioDto getIntermediaByConsorcio (Long idConsorcio) throws Exception{
        IntermediaExpensaConsorcio intermediaExpensaConsorcio = iIntermediaExpensaConsorcioRepository.findByIdConsorcio(idConsorcio);
        if(intermediaExpensaConsorcio == null){
            throw new Exception("No existe clase intermedia para el consorcio.");
        }
        return mapToIntermediaExpensaConsorcioResponse(intermediaExpensaConsorcio);
    }

    public void deleteIntermedia (Long idIntermedia) throws Exception{
        IntermediaExpensaConsorcio intermedia = iIntermediaExpensaConsorcioRepository.findById(idIntermedia)
                .orElseThrow(()-> new Exception("Intermedia no encontrada"));

        iIntermediaExpensaConsorcioRepository.delete(intermedia);
    }

    private IntermediaExpensaConsorcioDto mapToIntermediaExpensaConsorcioResponse(IntermediaExpensaConsorcio intermediaExpensaConsorcio) {
        IntermediaExpensaConsorcioDto dto = new IntermediaExpensaConsorcioDto();

        dto.setIdIntermedia(intermediaExpensaConsorcio.getIdIntermedia());
        dto.setIdConsorcio(intermediaExpensaConsorcio.getIdConsorcio());
        dto.setIdExpensa(intermediaExpensaConsorcio.getIdExpensa());

        return dto;
    }
    private void mapToIntermediaExpensaConsorcioUpdate(IntermediaExpensaConsorcio intermedia, IntermediaExpensaConsorcioDto dto) {
        intermedia.setIdExpensa(dto.getIdExpensa());
        iIntermediaExpensaConsorcioRepository.save(intermedia);
    }
    private IntermediaExpensaConsorcio mapToIntermediaExpensaConsorcioEntity(IntermediaExpensaConsorcioCreateDto dto) throws Exception {
        IntermediaExpensaConsorcio intermedia = iIntermediaExpensaConsorcioRepository.findByIdConsorcio(dto.getIdConsorcio());
        if(intermedia != null){
            throw new Exception("El consorcio ya tiene una clase intermedia.");
        }

        IntermediaExpensaConsorcio intermediaNew = new IntermediaExpensaConsorcio();

        intermediaNew.setIdConsorcio(dto.getIdConsorcio());
        intermediaNew.setIdExpensa(dto.getIdExpensa());

        return intermediaNew;
    }
}
