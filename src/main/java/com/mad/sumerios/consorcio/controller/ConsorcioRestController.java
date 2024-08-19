package com.mad.sumerios.consorcio.controller;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.service.ConsorcioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consorcio/")
public class ConsorcioRestController {

    private final ConsorcioService consorcioService;

    @Autowired
    public ConsorcioRestController(ConsorcioService consorcioService) {
        this.consorcioService = consorcioService;
    }

    //  CREAR CONSORCIO
    @PostMapping(value="create", headers = "Accept=application/json")
    public ResponseEntity<String> createConsorcio(@RequestBody Consorcio consorcio) {
        try {
            consorcioService.createConsorcio(consorcio);
            return ResponseEntity.status(HttpStatus.CREATED).body("Consorcio creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR CONSORCIOS
    @GetMapping(value = "get", headers = "Accept=application/json")
    public ResponseEntity<List<Consorcio>> getAllConsorcio (){
        List<Consorcio> consorcios = consorcioService.getConsorcios();
        if (consorcios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(consorcios);
    }

    //  ACTUALIZAR CONSORCIO
    @PutMapping(value = "update", headers = "Accept=application/json")
    public ResponseEntity<String> updateConsorcio(@RequestBody Consorcio consorcio) {
        try{
            consorcioService.updateConsorcio(consorcio);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Consorcio: " + consorcio.getNombre() + " - "+consorcio.getDireccion()+" modificado exitosamente"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR CONSORCIO
    @DeleteMapping(value = "delete/{id}", headers = "Accept=application/json")
    public ResponseEntity<String> deleteConsorcio(@PathVariable Long id){
        try {
            consorcioService.deleteConsorcio(id);
            return ResponseEntity.status(HttpStatus.OK).body("Consorcio eliminado exitosamente");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consorcio no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el consorcio: " + e.getMessage());
        }
    }
}
