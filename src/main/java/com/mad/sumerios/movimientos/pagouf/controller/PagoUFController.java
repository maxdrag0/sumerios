package com.mad.sumerios.movimientos.pagouf.controller;


import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFRequest;
import com.mad.sumerios.movimientos.pagouf.service.PagoUFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
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
    public ResponseEntity<byte[]> createPagoUF(@RequestBody PagoUFRequest pagoUF) {
        try {
            return pagoUFService.createPagoUF(pagoUF);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //  LISTAR
    //  por uf
    @GetMapping("/unidadFuncional/{idUf}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByUF (@PathVariable Long idUf){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByUnidadFuncional(idUf);
            if(pagoUFS.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por uf y periodo
    @GetMapping("/unidadFuncional/{IdUf}/periodo/{periodo}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByUFAndPeriodo (@PathVariable Long IdUf,
                                                                    @PathVariable YearMonth periodo){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByUnidadFuncionalAndPeriodo(IdUf, periodo);
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
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
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
    //  por consorcio y periodo
    @GetMapping("/consorcio/{idConsorcio}/periodo/{periodo}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByConsorcio (@PathVariable Long idConsorcio,
                                                                 @PathVariable YearMonth periodo){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByPeriodoAndConsorcio(periodo, idConsorcio);
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
                                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
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
    //  por periodo
    @GetMapping("/consorcio/{idConsorcio}/expensa/{periodo}")
    public ResponseEntity<List<PagoUFDTO>> getPagoUFByExpensa(@PathVariable Long idConsorcio,
                                                              @PathVariable YearMonth periodo){
        try{
            List<PagoUFDTO> pagoUFS = pagoUFService.getPagoUFByExpensa(idConsorcio, periodo);
            return ResponseEntity.ok(pagoUFS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //  ACTUALIZAR PAGO UF
//    @PutMapping("/{idPagoUf}")
//    public ResponseEntity<String> updatePagoUF( @PathVariable Long idPagoUf,
//                                                @RequestBody PagoUFUpdateDTO dto){
//        try{
//            pagoUFService.updatePagoUF(idPagoUf,dto);
//            return ResponseEntity.status(HttpStatus.OK).body("Pago actualizado exitosamente");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

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
