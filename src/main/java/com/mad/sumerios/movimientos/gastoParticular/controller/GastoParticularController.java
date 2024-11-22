package com.mad.sumerios.movimientos.gastoParticular.controller;

import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularCreateDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularResponseDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularUpdateDTO;
import com.mad.sumerios.movimientos.gastoParticular.service.GastoParticularService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/gastos_particulares")
public class GastoParticularController {

    private final GastoParticularService gastoParticularService;

    public GastoParticularController(GastoParticularService gastoParticularService) {
        this.gastoParticularService = gastoParticularService;
    }

    // CRUD
    // Create
    @PostMapping
    public ResponseEntity<String> createGastoParticular(@RequestBody GastoParticularCreateDTO dto){
        try {
            gastoParticularService.createGastoParticular(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Gasto particular creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update
    @PutMapping("/{idGastoParticular}")
    public ResponseEntity<String> updateGastoParticular (@PathVariable Long idGastoParticular,
                                                         @RequestBody GastoParticularUpdateDTO dto){
        try{
            gastoParticularService.updateGastoParticular(idGastoParticular, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Gasto particular modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete
    @DeleteMapping("/{idGastoParticular}")
    public ResponseEntity<String> deleteGastoParticular (@PathVariable Long idGastoParticular){
        try{
            gastoParticularService.deleteGastoParticular(idGastoParticular);
            return ResponseEntity.status(HttpStatus.OK).body("Gasto particular eliminado exitosamente");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Egreso no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el egreso: " + e.getMessage());
        }
    }

    // Gets by
    // proveedor
    @GetMapping("/proveedores/{idProveedor}")
    public ResponseEntity<List<GastoParticularResponseDTO>> getGastoParticularByProveedor (@PathVariable Long idProveedor){
        try{
            List<GastoParticularResponseDTO> gastos = gastoParticularService.findByProveedor(idProveedor);
            if(gastos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // consorcio
    @GetMapping("/consorcios/{idConsorcio}")
    public ResponseEntity<List<GastoParticularResponseDTO>> getGastoParticularByConsorcio (@PathVariable Long idConsorcio){
        try{
            List<GastoParticularResponseDTO> gastos = gastoParticularService.findByConsorcio(idConsorcio);
            if(gastos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // consorcio y fecha
    @GetMapping("/consorcios/{idConsorcio}/dates")
    public ResponseEntity<List<GastoParticularResponseDTO>> getGastoParticularByConsorcioAndDate (@PathVariable Long idConsorcio,
                                                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
        try{
            List<GastoParticularResponseDTO> gastos = gastoParticularService.findByConsorcioAndFecha(idConsorcio, startDate, endDate);
            if(gastos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // uf
    @GetMapping("/unidadFuncional/{idUf}")
    public ResponseEntity<List<GastoParticularResponseDTO>> getGastoParticularByUf (@PathVariable Long idUf){
        try{
            List<GastoParticularResponseDTO> gastos = gastoParticularService.findByUf(idUf);
            if(gastos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // uf y fecha
    @GetMapping("/unidadFuncional/{idUf}/dates")
    public ResponseEntity<List<GastoParticularResponseDTO>> getGastoParticularByUfAndDate (@PathVariable Long idUf,
                                                                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate){
        try{
            List<GastoParticularResponseDTO> gastos = gastoParticularService.findByUfAndFecha(idUf, startDate, endDate);
            if(gastos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // total final
    @GetMapping("/total/{totalFinal}")
    public ResponseEntity<List<GastoParticularResponseDTO>> getGastoParticularByUf (@PathVariable Double totalFinal){
        try{
            List<GastoParticularResponseDTO> gastos = gastoParticularService.findByTotalFinal(totalFinal);
            if(gastos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por comprobante
    @GetMapping("/comprobantes/{comprobante}")
    public ResponseEntity<?> getGastoParticularByComprobante (@PathVariable String comprobante){
        try{
            GastoParticularResponseDTO gastos = gastoParticularService.getGastoParticularByComprobante(comprobante);
            return ResponseEntity.ok(gastos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    //  por periodo
//    @GetMapping("/consorcios/{idConsorcio}/expensa/{periodo}")
//    public ResponseEntity<List<GastoParticularResponseDTO>> getGastoParticularByExpensa(@PathVariable Long idConsorcio,
//                                                                                        @PathVariable YearMonth periodo){
//        try{
//            List<GastoParticularResponseDTO> gastos = gastoParticularService.getGastoParticularByExpensa(idConsorcio, periodo);
//            return ResponseEntity.ok(gastos);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }



}
