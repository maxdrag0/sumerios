package com.mad.sumerios.consorcio.service;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsorcioService {

    private final IConsorcioRepository consorcioRepository;

    @Autowired
    public ConsorcioService(IConsorcioRepository consorcioRepository) {
        this.consorcioRepository = consorcioRepository;
    }

    //  CREAR CONSORCIO
    public void createConsorcio(Consorcio consorcio) throws Exception {
        if(consorcioRepository.findByNombre(consorcio.getNombre()).isPresent()){
            throw new Exception("El consorcio ya está registrado (nombre ya existente)");
        } else if (consorcioRepository.findByDireccion(consorcio.getDireccion()).isPresent()){
            throw new Exception("El consorcio ya está registrado. (Direccion ya existente)");
        }
        consorcioRepository.save(consorcio);
    }

    //  LISTAR CONSORCIOS
    public List<Consorcio> getConsorcios(){
        return consorcioRepository.findAll();
    }

    //  ACTUALIZAR CONSORCIO
    public void updateConsorcio(Consorcio consorcio) throws Exception {
        Consorcio cons = consorcioRepository.findById(consorcio.getIdConsorcio())
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));

        if (consorcioRepository.findByNombre(consorcio.getNombre())
                .filter(a -> a.getIdConsorcio() != consorcio.getIdConsorcio()).isPresent()) {
            throw new Exception("El consorcio ya está registrado\nNombre: "+consorcio.getNombre()+" ya existente");
        } else if(consorcioRepository.findByDireccion(consorcio.getDireccion())
                .filter(a -> a.getIdConsorcio() != consorcio.getIdConsorcio()).isPresent()){
            throw new Exception("El consorcio ya está registrado\nMail: "+consorcio.getDireccion()+" ya existente");
        }

        cons.setNombre(consorcio.getNombre());
        cons.setDireccion(consorcio.getDireccion());

        consorcioRepository.save(cons);
    }

    //  ELIMINAR CONSORCIO
    public void deleteConsorcio(Long id) throws Exception{
        Consorcio consorcio = consorcioRepository.findById(id)
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));

        consorcioRepository.delete(consorcio);
    }
}
