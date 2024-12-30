package com.mad.sumerios.movimientos.ingreso.controller;


import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoCreateDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoResponseDTO;
import com.mad.sumerios.movimientos.ingreso.dto.IngresoUpdateDTO;
import com.mad.sumerios.movimientos.ingreso.service.IngresoService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/ingresos")
public class IngresoController {

    private final IngresoService ingresoService;

    @Autowired
    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

    //  CREAR INGRESO
    @PostMapping
    public ResponseEntity<String> createIngreso(@RequestBody IngresoCreateDTO dto) {
        try {
            ingresoService.createIngreso(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR
    // por consorcios
    @GetMapping("/consorcios/{idConsorcio}")
    public ResponseEntity<List<IngresoResponseDTO>> getIngresoByConsorcio (@PathVariable Long idConsorcio){
        try{
            List<IngresoResponseDTO> ingresos = ingresoService.getIngresoByConsorcio(idConsorcio);
            if(ingresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // por consorcios
    @GetMapping("/consorcio/{idConsorcio}/periodo/{periodo}")
    public ResponseEntity<List<IngresoResponseDTO>> getIngresoByPeriodoAndConsorcio (@PathVariable Long idConsorcio, @PathVariable YearMonth periodo){
        try{
            List<IngresoResponseDTO> ingresos = ingresoService.getIngresoByPeriodoAndConsorcio(periodo, idConsorcio);
            if(ingresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por consorcio y fechas
    @GetMapping("/consorcios/{idConsorcio}/dates")
    public ResponseEntity<List<IngresoResponseDTO>> getIngresoByConsorcioYFecha (@PathVariable Long idConsorcio,
                                                                                 @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                                                                 @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate){
        try{
            List<IngresoResponseDTO> ingresos = ingresoService.getIngresoByConsorcioYFecha(idConsorcio, startDate, endDate);
            if(ingresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // por proveedor
    @GetMapping("/proveedores/{idProveedor}")
    public ResponseEntity<List<IngresoResponseDTO>> getIngresoByProveedor (@PathVariable Long idProveedor){
        try{
            List<IngresoResponseDTO> ingresos = ingresoService.getIngresoByProveedor(idProveedor);
            if(ingresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // por proveedor y consorcio
    @GetMapping("/proveedores/{idProveedor}/consorcios/{idConsorcio}")
    public ResponseEntity<List<IngresoResponseDTO>> getIngresoByProveedorAndConsorcio (@PathVariable Long idProveedor,
                                                                                       @PathVariable Long idConsorcio){
        try{
            List<IngresoResponseDTO> ingresos = ingresoService.getIngresoByProveedorAndConsorcio(idProveedor, idConsorcio);
            if(ingresos.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //  por periodo
    @GetMapping("/consorcio/{idConsorcio}/expensa/{periodo}")
    public ResponseEntity<List<IngresoResponseDTO>> getIngresoByExpensa(@PathVariable Long idConsorcio,
                                                                       @PathVariable YearMonth periodo){
        try{
            List<IngresoResponseDTO> ingresos = ingresoService.getIngresoByExpensa(idConsorcio, periodo);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //  ACTUALIZAR INGRESO
    @PutMapping("/{idIngreso}")
    public ResponseEntity<String> updateIngreso(@PathVariable Long idIngreso,
                                                @RequestBody IngresoUpdateDTO dto){
        try{
            ingresoService.updateIngreso(idIngreso, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Ingreso actualizado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  BORRAR INGRESO
    @DeleteMapping("/{idIngreso}")
    public ResponseEntity<String> deleteIngreso(@PathVariable Long idIngreso){
        try{
            ingresoService.deleteIngreso(idIngreso);
            return ResponseEntity.status(HttpStatus.OK).body("Ingreso eliminado exitosamente");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingreso no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el Ingreso: " + e.getMessage());
        }
    }

}
