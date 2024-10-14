//package com.mad.sumerios.pendientes.movimientos.ingreso.controller;
//
//
//import com.mad.sumerios.pendientes.movimientos.ingreso.model.Ingreso;
//import com.mad.sumerios.pendientes.movimientos.ingreso.service.IngresoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Date;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/ingreso/")
//public class IngresoController {
//
//    private final IngresoService ingresoService;
//
//    @Autowired
//    public IngresoController(IngresoService ingresoService) {
//        this.ingresoService = ingresoService;
//    }
//
//    //  CREAR INGRESO
//    @PostMapping(name="create" , headers = "Accept=application/json")
//    public ResponseEntity<String> createIngreso(@RequestBody Ingreso ingreso) {
//        try {
//            ingresoService.createIngreso(ingreso);
//            return ResponseEntity.status(HttpStatus.CREATED).body("Ingreso creado exitosamente");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    //  LISTAR
//    //  POR CONSORCIO Y FECHA
//    @GetMapping(name="consorcio/{consorcioId}", headers = "Accept=application/json")
//    public ResponseEntity<List<Ingreso>> getIngresoPorConsorcioYFecha (@PathVariable Long consorcioId,
//                                                                       @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
//                                                                       @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin){
//        List<Ingreso> ingresos = ingresoService.getIngresoPorConsorcioYFecha(consorcioId, fechaInicio, fechaFin);
//        if(ingresos.isEmpty()){
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.ok(ingresos);
//    }
//
//    //  ACTUALIZAR INGRESO
//    @PutMapping(name="update" , headers = "Accept=application/json")
//    public ResponseEntity<String> updateIngreso(@RequestBody Ingreso ingreso){
//        try{
//            ingresoService.updateIngreso(ingreso);
//            return ResponseEntity.status(HttpStatus.OK).body("Ingreso actualizado exitosamente");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    //  BORRAR INGRESO
//    @DeleteMapping(name="delete/{id}" , headers = "Accept=application/json")
//    public ResponseEntity<String> deleteIngreso(@PathVariable Long id){
//        try{
//            ingresoService.deleteIngreso(id);
//            return ResponseEntity.status(HttpStatus.OK).body("Ingreso eliminado exitosamente");
//        } catch (EmptyResultDataAccessException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingreso no encontrado");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el Ingreso: " + e.getMessage());
//        }
//    }
//
//}
