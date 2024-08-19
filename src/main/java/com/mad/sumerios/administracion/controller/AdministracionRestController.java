package com.mad.sumerios.administracion.controller;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.service.AdministracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administracion/")
public class AdministracionRestController {

    private final AdministracionService admService;

    @Autowired
    public AdministracionRestController(AdministracionService admService) {
        this.admService = admService;
    }

    //  CREAR ADMINISTRACION
    @PostMapping(value = "create", headers = "Accept=application/json")
    public ResponseEntity<String> createAdministracion(@RequestBody Administracion administracion) {
        try {
            admService.createAdministracion(administracion);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    "Administración creada exitosamente"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR ADMINISTRACIONES
    @GetMapping(value = "get", headers = "Accept=application/json")
    public ResponseEntity<List<Administracion>> getAllAdministraciones() {
        List<Administracion> administraciones = admService.getAdministraciones();
        if (administraciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(administraciones);
    }

    // ACTUALIZAR ADMINISTRACION
    @PutMapping(value = "update", headers = "Accept=application/json")
    public ResponseEntity<String> updateAdministracion(@RequestBody Administracion adm){
        try {
            admService.updateAdministracion(adm);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Administración: " + adm.getNombre() + " modificada exitosamente"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR ADMINISTRACION
    @DeleteMapping(value = "delete/{id}", headers = "Accept=application/json")
    public ResponseEntity<String> deleteAdministracion(@PathVariable Long id) {
        try {
            admService.deleteAdministracion(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Administración eliminada exitosamente"
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
