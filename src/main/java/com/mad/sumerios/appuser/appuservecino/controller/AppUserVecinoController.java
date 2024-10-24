package com.mad.sumerios.appuser.appuservecino.controller;

import com.mad.sumerios.appuser.appuservecino.model.AppUserVecino;
import com.mad.sumerios.appuser.appuservecino.service.AppUserVecinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vecinos")
public class AppUserVecinoController {

    private final AppUserVecinoService appUserVecinoService;

    @Autowired
    public AppUserVecinoController(AppUserVecinoService appUserVecinoService) {
        this.appUserVecinoService = appUserVecinoService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserVecino> updateVecino(@PathVariable Long id, @RequestBody AppUserVecino updatedVecino) {
        AppUserVecino vecino = appUserVecinoService.updateVecino(id, updatedVecino);
        return ResponseEntity.ok(vecino);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVecino(@PathVariable Long id) {
        appUserVecinoService.deleteVecino(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserVecino> getVecinoById(@PathVariable Long id) {
        Optional<AppUserVecino> vecino = appUserVecinoService.getVecinoById(id);
        return vecino.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

