package com.mad.sumerios.appuser.appuseradmin.controller;

import com.mad.sumerios.appuser.appuseradmin.dto.AppUserAdminResponseDTO;
import com.mad.sumerios.appuser.appuseradmin.dto.AppUserAdminUpdateDTO;
import com.mad.sumerios.appuser.appuseradmin.service.AppUserAdminService;
import com.mad.sumerios.appuser.dto.AppUserResponseDTO;
import com.mad.sumerios.appuser.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins")
public class AppUserAdminController {

    private final AppUserAdminService appUserAdminService;
    private final AppUserService appUserService;

    @Autowired
    public AppUserAdminController(AppUserAdminService appUserAdminService,
                                  AppUserService appUserService) {
        this.appUserAdminService = appUserAdminService;
        this.appUserService = appUserService;
    }

    //    EDITAR ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody AppUserAdminUpdateDTO dto) {
        try {
            AppUserAdminResponseDTO response = appUserAdminService.updateAdmin(id, dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    //    DELETE ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        try{
            appUserService.deleteUser(id);
            return ResponseEntity.ok("Administrador eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el administrador: " + e.getMessage());
        }
    }

    //    OBTENER ADMIN POR MATRICULA
    @GetMapping("/{matricula}")
    public ResponseEntity<?> getAdminMatricula(@PathVariable String matricula) {
        try {
            AppUserAdminResponseDTO dto = appUserAdminService.getAdminByMatricula(matricula);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        try {
            AppUserAdminResponseDTO response = appUserAdminService.getAppUserByUsername(username);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}

