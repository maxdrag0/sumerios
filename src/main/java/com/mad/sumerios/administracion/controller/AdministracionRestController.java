package com.mad.sumerios.administracion.controller;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.service.AdministracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administraciones")
public class AdministracionRestController {

    private final AdministracionService administracionService;

    public AdministracionRestController(AdministracionService administracionService) {
        this.administracionService = administracionService;
    }

    @PostMapping
    public ResponseEntity<String> createAdministracion(@RequestBody Administracion administracion) {
        try {
            administracionService.createAdministracion(administracion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Administración creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Administracion>> getAllAdministraciones() {
        List<Administracion> administraciones = administracionService.getAdministraciones();
        if (administraciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(administraciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administracion> getAdministracionById(@PathVariable Long id) {
        Optional<Administracion> administracion = administracionService.getAdministracionById(id);
        return administracion.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAdministracion(@PathVariable Long id, @RequestBody Administracion administracion) {
        try {
            administracion.setIdAdm(id);
            administracionService.updateAdministracion(administracion);
            return ResponseEntity.status(HttpStatus.OK).body("Administración actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdministracion(@PathVariable Long id) {
        try {
            administracionService.deleteAdministracion(id);
            return ResponseEntity.status(HttpStatus.OK).body("Administración eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la administración: " + e.getMessage());
        }
    }

}
