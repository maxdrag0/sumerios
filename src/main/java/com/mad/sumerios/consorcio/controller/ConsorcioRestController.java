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
@RequestMapping("/api/administraciones/{idAdm}/consorcios")
public class ConsorcioRestController {

    private final ConsorcioService consorcioService;

    @Autowired
    public ConsorcioRestController(ConsorcioService consorcioService) {
        this.consorcioService = consorcioService;
    }

    //  CREAR CONSORCIO
    @PostMapping
    public ResponseEntity<Map<String, Object>> createConsorcio(@PathVariable Long idAdm,
                                                               @RequestBody Consorcio consorcio) {
        Map<String, Object> response = new HashMap<>();
        try {
            Consorcio nuevoConsorcio = consorcioService.createConsorcio(idAdm,consorcio);

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
    public ResponseEntity<List<Consorcio>> getConsorcios (@PathVariable Long idAdm){
        try {
            List<Consorcio> consorcios = consorcioService.getConsorciosPorAdministracion(idAdm);
            if (consorcios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(consorcios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //  ACTUALIZAR CONSORCIO
    @PutMapping("/{idConsorcio}")
    public ResponseEntity<String> updateConsorcio(@PathVariable Long idAdm,
                                                  @PathVariable Long idConsorcio,
                                                  @RequestBody Consorcio consorcio) {
        try{
            consorcioService.updateConsorcio(idAdm, idConsorcio, consorcio);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Consorcio: " + consorcio.getNombre() + " - "+consorcio.getDireccion()+" modificado exitosamente"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR CONSORCIO
    @DeleteMapping("/{idConsorcio}")
    public ResponseEntity<String> deleteConsorcio(@PathVariable Long idAdm,
                                                  @PathVariable Long idConsorcio){
        try {
            consorcioService.deleteConsorcio(idAdm, idConsorcio);
            return ResponseEntity.status(HttpStatus.OK).body("Consorcio eliminado exitosamente");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consorcio no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el consorcio: " + e.getMessage());
        }
    }
}
