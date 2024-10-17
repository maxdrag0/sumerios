package com.mad.sumerios.auth.controller;

import com.mad.sumerios.appuser.dto.AppUserRegisterDTO;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.appuseradmin.dto.AppUserAdminRegisterDTO;
import com.mad.sumerios.appuseradmin.model.AppUserAdmin;
import com.mad.sumerios.appuseradmin.service.AppUserAdminService;
import com.mad.sumerios.appuservecino.dto.AppUserVecinoRegisterDTO;
import com.mad.sumerios.appuservecino.service.AppUserVecinoService;
import com.mad.sumerios.auth.service.LoginRequest;
import com.mad.sumerios.enums.RolUser;
import com.mad.sumerios.utils.JwtResponse;
import com.mad.sumerios.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserAdminService appUserAdminService;
    private final AppUserVecinoService appUserVecinoService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          AppUserAdminService appUserAdminService,
                          AppUserVecinoService appUserVecinoService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.appUserAdminService = appUserAdminService;
        this.appUserVecinoService = appUserVecinoService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/registerAdmin", consumes = "application/json")
    public ResponseEntity<String> createAdmin(@RequestBody AppUserAdminRegisterDTO admin) {
        try{
            appUserAdminService.createAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admnistrador creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/registerVecino", consumes = "application/json")
    public ResponseEntity<String> createVecino(@RequestBody AppUserVecinoRegisterDTO admin) {
        try{
            appUserVecinoService.createVecino(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admnistrador creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Establecer el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar el token JWT
        String jwtToken = jwtUtil.generateToken(authentication);

        // Devolver el token JWT como respuesta
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }


}


