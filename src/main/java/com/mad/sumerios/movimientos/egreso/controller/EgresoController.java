package com.mad.sumerios.movimientos.egreso.controller;

import com.mad.sumerios.movimientos.egreso.dto.EgresoCreateDTO;
import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.egreso.dto.EgresoUpdateDTO;
import com.mad.sumerios.movimientos.egreso.service.EgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/egresos")
public class EgresoController {

    private final EgresoService egresoService;

    @Autowired
    public EgresoController (EgresoService egresoService) {
        this.egresoService = egresoService;
    }

    //  CREAR EGRESO
    @PostMapping
    public ResponseEntity<String> createEgreso (@RequestBody EgresoCreateDTO dto) {
        try {
            egresoService.createEgreso(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Egreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR EGRESOS
    //  por consorcio y fechas
    @GetMapping("/consorcio/{idConsorcio}/dates")
    public ResponseEntity<List<EgresoResponseDTO>> getEgresosByIdConsorcioAndFecha (@PathVariable Long idConsorcio,
                                                                                    @RequestParam Date startDate,
                                                                                    @RequestParam Date endDate){
        try{
            List<EgresoResponseDTO> egresos = egresoService.getEgresosByIdConsorcioAndFecha(idConsorcio, startDate, endDate);
            if(egresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(egresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por consorcio
    @GetMapping("/consorcio/{idConsorcio}")
    public ResponseEntity<List<EgresoResponseDTO>> getEgresosByIdConsorcio (@PathVariable Long idConsorcio){
        try{
            List<EgresoResponseDTO> egresos = egresoService.getEgresosByIdConsorcio(idConsorcio);
            if(egresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(egresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por proveedor
    @GetMapping("/proveedores/{idProveedor}")
    public ResponseEntity<List<EgresoResponseDTO>> getEgresosByProveedor (@PathVariable Long idProveedor){
        try{
            List<EgresoResponseDTO> egresos = egresoService.getEgresosByProveedor(idProveedor);
            if(egresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(egresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por proveedor y consorcio
    @GetMapping("/proveedores/{idProveedor}/consorcio/{idConsorcio}")
    public ResponseEntity<List<EgresoResponseDTO>> getEgresosByProveedorAndConsorcio (@PathVariable Long idProveedor,
                                                                                      @PathVariable Long idConsorcio){
        try{
            List<EgresoResponseDTO> egresos = egresoService.getEgresosByProveedorAndConsorcio(idProveedor, idConsorcio);
            if(egresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(egresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por total final
    @GetMapping("/totales/{totalFinal}")
    public ResponseEntity<List<EgresoResponseDTO>> getEgresosByTotalFinal (@PathVariable Double totalFinal){
        try{
            List<EgresoResponseDTO> egresos = egresoService.getEgresosByTotalFinal(totalFinal);
            if(egresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(egresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por comprobante
    @GetMapping("/comprobantes/{comprobante}")
    public ResponseEntity<?> getEgresosByComprobante (@PathVariable String comprobante){
        try{
            EgresoResponseDTO egreso = egresoService.getEgresosByComprobante(comprobante);
            return ResponseEntity.ok(egreso);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    //  por periodo
    @GetMapping("/consorcio/{idConsorcio}/expensa/{periodo}")
    public ResponseEntity<List<EgresoResponseDTO>> getEgresosByExpensa(@PathVariable Long idConsorcio,
                                                                       @PathVariable YearMonth periodo){
        try{
            List<EgresoResponseDTO> egresos = egresoService.getEgresosByExpensa(idConsorcio, periodo);
            return ResponseEntity.ok(egresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //  ACTUALIZAR EGRESO
    @PutMapping ("/{idIngreso}")
    public ResponseEntity<String> updateEgreso (@PathVariable Long idIngreso,
                                                @RequestBody EgresoUpdateDTO dto){
        try{
            egresoService.updateEgreso(idIngreso, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Egreso modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR EGRESO
    @DeleteMapping ("/{idEgreso}")
    public ResponseEntity<String> deleteEgreso (@PathVariable Long idEgreso){
        try{
            egresoService.deleteEgreso(idEgreso);
            return ResponseEntity.status(HttpStatus.OK).body("Egreso eliminado exitosamente");
        }catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Egreso no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el egreso: " + e.getMessage());
        }
    }
}
