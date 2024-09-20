package com.mad.sumerios.login.controller;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.service.AdministracionService;
import com.mad.sumerios.login.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AdministracionService administracionService;

    // Inyección de dependencias por constructor
    public LoginController(AdministracionService administracionService) {
        this.administracionService = administracionService;
    }

    @PostMapping("/login")
    public ResponseEntity<Administracion> login(@RequestBody LoginRequest loginRequest) {
        try {
            Administracion administracion = administracionService.login(loginRequest.getMail(), loginRequest.getPassword());
            return ResponseEntity.ok(administracion);  // Devuelve la administración si el login es exitoso
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // 401 si las credenciales son incorrectas
        }
    }
}

