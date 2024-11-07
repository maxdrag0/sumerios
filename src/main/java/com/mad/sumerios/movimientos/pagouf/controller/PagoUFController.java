package com.mad.sumerios.movimientos.pagouf.controller;


import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFCreateDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFUpdateDTO;
import com.mad.sumerios.movimientos.pagouf.service.PagoUFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/pagoUF")
public class PagoUFController {

    private final PagoUFService pagoUFService;

    @Autowired
    public PagoUFController(PagoUFService pagoUFService) {
        this.pagoUFService = pagoUFService;
    }

    //  CREAR PAGO UF
    @PostMapping
    public ResponseEntity<String> createPagoUF(@RequestBody PagoUFCreateDTO pagoUF) {
        try {
            pagoUFService.createPagoUF(pagoUF);
            return ResponseEntity.status(HttpStatus.CREATED).body("Pago creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR
    //  por uf
    @GetMapping("/unidadFuncional/{ufId}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByUF (@PathVariable Long ufId){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByUnidadFuncional(ufId);
            if(pagoUFS.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por uf y fecha
    @GetMapping("unidadFuncional/{ufId}/dates")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByUFAndFecha (@PathVariable Long ufId,
                                                                  @RequestParam Date startDate,
                                                                  @RequestParam Date endDate
    ){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagosByUnidadFuncionalAndFecha(ufId,startDate, endDate);
            if(pagoUFS.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por consorcio
    @GetMapping("/consorcio/{idConsorcio}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByConsorcio (@PathVariable Long idConsorcio){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByConsorcio(idConsorcio);
            if(pagoUFS.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por consorcio y fecha
    @GetMapping("/consorcio/{idConsorcio}/dates")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByConsorcio (@PathVariable Long idConsorcio,
                                                                 @RequestParam Date startDate,
                                                                 @RequestParam Date endDate){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByConsorcioAndFecha(idConsorcio,
                                                                                 startDate,
                                                                                 endDate);
            if(pagoUFS.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por consorcio y forma de pago
    @GetMapping("/consorcio/{idConsorcio}/formaPago/{formaPago}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByConsorcioAndFormaPago (@PathVariable Long idConsorcio,
                                                                             @PathVariable FormaPago formaPago){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByConsorcioAndFormaPago(idConsorcio,formaPago);
            if(pagoUFS.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por forma de pago
    @GetMapping("/formaPago/{formaPago}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByFormaPago (@PathVariable FormaPago formaPago){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByFormaPago(formaPago);
            if(pagoUFS.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //  ACTUALIZAR PAGO UF
    @PutMapping("/{idPagoUf}")
    public ResponseEntity<String> updatePagoUF( @PathVariable Long idPagoUf,
                                                @RequestBody PagoUFUpdateDTO dto){
        try{
            pagoUFService.updatePagoUF(idPagoUf,dto);
            return ResponseEntity.status(HttpStatus.OK).body("Pago actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  BORRAR PAGO UF
    @DeleteMapping("/{idPagoUF}")
    public ResponseEntity<String> deletePagoUF(@PathVariable Long idPagoUF){
        try{
            pagoUFService.deletePagoUF(idPagoUF);
            return ResponseEntity.status(HttpStatus.OK).body("Pago eliminado exitosamente");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pago no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el Pago: " + e.getMessage());
        }
    }

}
