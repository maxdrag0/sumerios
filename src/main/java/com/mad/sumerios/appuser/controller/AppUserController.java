package com.mad.sumerios.appuser.controller;

import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.appuser.dto.AppUserResponseDTO;
import com.mad.sumerios.appuser.service.AppUserService;
import com.mad.sumerios.appuseradmin.dto.AppUserAdminResponseDTO;
import com.mad.sumerios.appuseradmin.service.AppUserAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try {
            AppUserResponseDTO response = appUserService.getAppUserById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        try {
            AppUserResponseDTO response = appUserService.getAppUserByUsername(username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/mail/{mail}")
    public ResponseEntity<?> getUserByMail(@PathVariable String mail){
        try {
            AppUserResponseDTO response = appUserService.getAppUserByMail(mail);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponseDTO>> getAllUsers() {
        List<AppUserResponseDTO> users = appUserService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
}
