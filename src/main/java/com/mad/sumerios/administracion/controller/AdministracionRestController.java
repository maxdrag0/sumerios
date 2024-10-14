package com.mad.sumerios.administracion.controller;

import com.mad.sumerios.administracion.dto.AdministracionRegisterDTO;
import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.service.AdministracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/administraciones")
public class AdministracionRestController {

    private final AdministracionService administracionService;

    @Autowired
    public AdministracionRestController(AdministracionService administracionService) {
        this.administracionService = administracionService;
    }

    // Crear una nueva administración
    @PostMapping
    public ResponseEntity<String> createAdministracion(@RequestBody AdministracionRegisterDTO dto) {
        try {
            administracionService.createAdministracion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Administración creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Obtener todas las administraciones
    @GetMapping
    public ResponseEntity<List<AdministracionResponseDTO>> getAllAdministraciones() {
        List<AdministracionResponseDTO> administraciones = administracionService.getAdministraciones();
        if (administraciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(administraciones);
    }

    // Obtener una administración por ID
    @GetMapping("/{idAdm}")
    public ResponseEntity<?> getAdministracionById(@PathVariable Long idAdm) {
        try {
            AdministracionResponseDTO administracion = administracionService.getAdministracionById(idAdm);
            return ResponseEntity.ok(administracion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    // Actualizar una administración
    @PutMapping("/{idAdm}")
    public ResponseEntity<String> updateAdministracion(@PathVariable Long idAdm, @RequestBody AdministracionRegisterDTO dto) {
        try {
            administracionService.updateAdministracion(idAdm, dto);
            return ResponseEntity.ok("Administración actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Eliminar una administración
    @DeleteMapping("/{idAdm}")
    public ResponseEntity<String> deleteAdministracion(@PathVariable Long idAdm) {
        try {
            administracionService.deleteAdministracion(idAdm);
            return ResponseEntity.ok("Administración eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar la administración: " + e.getMessage());
        }
    }
}

