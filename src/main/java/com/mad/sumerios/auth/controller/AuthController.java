package com.mad.sumerios.auth.controller;

import com.mad.sumerios.appuser.appuseradmin.dto.AppUserAdminRegisterDTO;
import com.mad.sumerios.appuser.appuseradmin.service.AppUserAdminService;
import com.mad.sumerios.appuser.appuservecino.dto.AppUserVecinoRegisterDTO;
import com.mad.sumerios.appuser.appuservecino.service.AppUserVecinoService;
import com.mad.sumerios.auth.dto.LoginRequestDTO;
import com.mad.sumerios.auth.service.AppUserDetailsService;
import com.mad.sumerios.utils.JwtResponse;
import com.mad.sumerios.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserAdminService appUserAdminService;
    private final AppUserVecinoService appUserVecinoService;
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService userDetailsService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          AppUserAdminService appUserAdminService,
                          AppUserVecinoService appUserVecinoService,
                          JwtUtil jwtUtil,
                          AppUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.appUserAdminService = appUserAdminService;
        this.appUserVecinoService = appUserVecinoService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<String> createAdmin(@RequestBody AppUserAdminRegisterDTO admin) {
        try{
            appUserAdminService.createAdmin(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admnistrador creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/registerVecino")
    public ResponseEntity<String> createVecino(@RequestBody AppUserVecinoRegisterDTO admin) {
        try{
            appUserVecinoService.createVecino(admin);
            return ResponseEntity.status(HttpStatus.CREATED).body("Admnistrador creado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public JwtResponse authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar el JWT
        String jwt = jwtUtil.generateJwtToken(authentication);

        // Obtener detalles del usuario
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Retornar la respuesta JWT con los detalles del usuario
        return new JwtResponse(jwt, userDetails.getUsername());
    }

    @GetMapping("/status")
    public ResponseEntity<String> checkStatus() {
        return ResponseEntity.ok("ready");
    }
}


