package com.mad.sumerios.pendientes.movimientos.egreso.controller;

import com.mad.sumerios.pendientes.movimientos.egreso.model.Egreso;
import com.mad.sumerios.pendientes.movimientos.egreso.service.EgresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/egreso/")
public class EgresoController {

    private final EgresoService egresoService;

    @Autowired
    public EgresoController (EgresoService egresoService) {
        this.egresoService = egresoService;
    }

    //  CREAR EGRESO
    @PostMapping (value = "create", headers = "Accept=application/json")
    public ResponseEntity<String> createEgreso (@RequestBody Egreso egreso) {
        try {
            egresoService.createEgreso(egreso);
            return ResponseEntity.status(HttpStatus.CREATED).body("Egreso creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR EGRESOS
    @GetMapping(value = "consorcio/{consorcioId}", headers = "Accept=application/json")
    public ResponseEntity<List<Egreso>> getEgresosPorFechasYConsorcio (@PathVariable Long consorcioId,
                                                                       @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaInicio,
                                                                       @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaFin){
        List<Egreso> egresos = egresoService.getEgresosPorConsorcioYFechas(consorcioId, fechaInicio, fechaFin);

        if (egresos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(egresos);
    }

    @GetMapping(value = "proveedor/{idProveedor}", headers = "Accept=application/json")
    public ResponseEntity<List<Egreso>> getEgresosPorProveedor (@PathVariable Long idProveedor
                                                                       ){
        List<Egreso> egresos = egresoService.getEgresosPorProveedor(idProveedor);

        if (egresos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(egresos);
    }

    //  ACTUALIZAR EGRESO
    @PutMapping (value = "update", headers = "Accept=application/json")
    public ResponseEntity<String> updateEgreso (@RequestBody Egreso egreso){
        try{
            egresoService.updateEgreso(egreso);
            return ResponseEntity.status(HttpStatus.OK).body("Egreso modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR EGRESO
    @DeleteMapping (value = "delete/{id}", headers = "Accept=application/json")
    public ResponseEntity<String> deleteEgreso (@PathVariable Long id){
        try{
            egresoService.deleteEgreso(id);
            return ResponseEntity.status(HttpStatus.OK).body("Egreso eliminado exitosamente");
        }catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Egreso no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el egreso: " + e.getMessage());
        }
    }
}
