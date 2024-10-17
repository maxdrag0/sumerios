package com.mad.sumerios.appuseradmin.controller;

import com.mad.sumerios.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.appuseradmin.service.AppUserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
public class AppUserAdminController {

    private final AppUserAdminService appUserAdminService;

    @Autowired
    public AppUserAdminController(AppUserAdminService appUserAdminService) {
        this.appUserAdminService = appUserAdminService;
    }


    @PutMapping("/{id}")
    public ResponseEntity<AppUserAdmin> updateAdmin(@PathVariable Long id, @RequestBody AppUserAdmin updatedAdmin) {
        AppUserAdmin admin = appUserAdminService.updateAdmin(id, updatedAdmin);
        return ResponseEntity.ok(admin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        appUserAdminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserAdmin> getAdminById(@PathVariable Long id) {
        Optional<AppUserAdmin> admin = appUserAdminService.getAdminById(id);
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

