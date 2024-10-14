package com.mad.sumerios.pendientes.movimientos.pagouf.controller;


import com.mad.sumerios.pendientes.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.pendientes.movimientos.pagouf.service.PagoUFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagoUF/")
public class PagoUFController {

    private final PagoUFService pagoUFService;

    @Autowired
    public PagoUFController(PagoUFService pagoUFService) {
        this.pagoUFService = pagoUFService;
    }

    //  CREAR INGRESO
    @PostMapping(name="create" , headers = "Accept=application/json")
    public ResponseEntity<String> createPagoUF(@RequestBody PagoUF pagoUF) {
        try {
            pagoUFService.createPagoUF(pagoUF);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pago creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR
    //  POR UF
//    @GetMapping(name="unidadFuncional/{ufId}", headers = "Accept=application/json")
//    public ResponseEntity<List<PagoUF>> getPagoUFPorUF (@PathVariable Long ufId){
//        List<PagoUF> pagoUFS = pagoUFService.getPagoUFPorUnidadFuncional(ufId);
//        if(pagoUFS.isEmpty()){
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(pagoUFS);
//    }


    //  ACTUALIZAR INGRESO
    @PutMapping(name="update" , headers = "Accept=application/json")
    public ResponseEntity<String> updatePagoUF(@RequestBody PagoUF pagoUF){
        try{
            pagoUFService.updatePagoUF(pagoUF);
            return ResponseEntity.status(HttpStatus.OK).body("Pago actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  BORRAR EGRESO
    @DeleteMapping(name="delete/{id}" , headers = "Accept=application/json")
    public ResponseEntity<String> deletePagoUF(@PathVariable Long id){
        try{
            pagoUFService.deletePagoUF(id);
            return ResponseEntity.status(HttpStatus.OK).body("Pago eliminado exitosamente");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el Pago: " + e.getMessage());
        }
    }

}
