package com.mad.sumerios.appuser.controller;

import com.mad.sumerios.appuser.dto.AppUserRegisterDTO;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.appuser.model.LoginRequest;
import com.mad.sumerios.appuser.service.AppUserService;
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
    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          AppUserService appUserService,
                          JwtUtil jwtUtil){
        this.authenticationManager=authenticationManager;
        this.appUserService=appUserService;
        this.jwtUtil=jwtUtil;
    }

    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody AppUserRegisterDTO appUserRegisterDTO) {
        System.out.println("AppUserDTO recibido: " + appUserRegisterDTO);
        try {
            AppUser newUser = appUserService.registerUser(appUserRegisterDTO);
            return ResponseEntity.ok("Usuario registrado con éxito: " + newUser.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // Iniciar sesión
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

