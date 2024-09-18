package com.mad.sumerios.consorcio.controller;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.service.ConsorcioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/consorcios")
public class ConsorcioRestController {

    private final ConsorcioService consorcioService;

    @Autowired
    public ConsorcioRestController(ConsorcioService consorcioService) {
        this.consorcioService = consorcioService;
    }

    //  CREAR CONSORCIO
    @PostMapping
    public ResponseEntity<Map<String, Object>> createConsorcio(@RequestBody Consorcio consorcio) {
        Map<String, Object> response = new HashMap<>();
        try {
            Consorcio nuevoConsorcio = consorcioService.createConsorcio(consorcio);

            response.put("id", nuevoConsorcio.getIdConsorcio());
            response.put("message", "Consorcio creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    //  LISTAR CONSORCIOS
    @GetMapping
    public ResponseEntity<List<Consorcio>> getAllConsorcio (){
        List<Consorcio> consorcios = consorcioService.getConsorcios();
        if (consorcios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(consorcios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consorcio> getConsorcioById(@PathVariable Long id) {
        Optional<Consorcio> consorcio = consorcioService.getConsorcioById(id);
        return consorcio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  ACTUALIZAR CONSORCIO
    @PutMapping("/{id}")
    public ResponseEntity<String> updateConsorcio(@PathVariable Long id,@RequestBody Consorcio consorcio) {
        try{
            consorcio.setIdConsorcio(id);
            consorcioService.updateConsorcio(consorcio);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Consorcio: " + consorcio.getNombre() + " - "+consorcio.getDireccion()+" modificado exitosamente"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR CONSORCIO
    @DeleteMapping("/{id}")
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
