package com.mad.sumerios.appuser.controller;

import com.mad.sumerios.appuser.dto.AppUserDTO;
import com.mad.sumerios.appuser.dto.AppUserSearchDTO;
import com.mad.sumerios.appuser.model.AppUser;
import com.mad.sumerios.appuser.service.AppUserService;
import com.mad.sumerios.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserController (AppUserService appUserService, PasswordEncoder passwordEncoder ){
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<AppUserSearchDTO> getUserById(@PathVariable Long id) {
        Optional<AppUser> user = appUserService.getAppUserById(id);
        if (user.isPresent()) {
            AppUserSearchDTO dto = mapToSearchDTO(user.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // BUSCAR POR MAIL
    @GetMapping("/mail/{mail}")
    public ResponseEntity<AppUserSearchDTO> getUserByMail(@PathVariable String mail) throws Exception {
        Optional<AppUser> user = appUserService.getAppUserByMail(mail);
        if (user.isPresent()) {
            AppUserSearchDTO dto = mapToSearchDTO(user.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // BUSCAR POR USERNAME
    @GetMapping("/username/{username}")
    public ResponseEntity<AppUserSearchDTO> getUserByUsername(@PathVariable String username) throws Exception {
        Optional<AppUser> user = appUserService.getAppUserByUsername(username);
        if (user.isPresent()) {
            AppUserSearchDTO dto = mapToSearchDTO(user.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppUserDTO> updateUser(@PathVariable Long id, @RequestBody AppUserDTO appUserDTO) {
        try {
            AppUser updatedUser = appUserService.updateUser(id, appUserDTO);

            // Mapear a DTO para la respuesta
            AppUserDTO updatedUserDTO = mapToDTO(updatedUser);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Método para cambiar la contraseña de un usuario
//    @PutMapping("/{id}/change-password")
//    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody String newPassword) {
//        Optional<AppUser> optionalUser = appUserService.getAppUserById(id);
//        if (optionalUser.isPresent()) {
//            AppUser user = optionalUser.get();
//
//            // Verificar que la nueva contraseña no sea igual a la anterior
//            if (passwordEncoder.matches(newPassword, user.getPassword())) {
//                return ResponseEntity.badRequest().body("La nueva contraseña no puede ser igual a la actual.");
//            }
//
//            // Actualizar la contraseña
//            user.setPassword(passwordEncoder.encode(newPassword));
//            appUserService.saveUser(user);
//
//            return ResponseEntity.ok("Contraseña actualizada con éxito.");
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<AppUser> userOptional = appUserService.getAppUserById(id);
        if (userOptional.isPresent()) {
            appUserService.deleteUserById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // MAPEO PARA DTO
    private AppUserSearchDTO mapToSearchDTO(AppUser user) {
        AppUserSearchDTO dto = new AppUserSearchDTO();
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setUsername(user.getUsername());
        dto.setRolUser(String.valueOf(user.getRolUser()));
        dto.setMail(user.getMail());
        dto.setTelefono(user.getTelefono());
        dto.setPassword(user.getPassword());

        // Solo si el usuario tiene matricula de administrador
        if (user.getMatriculaAdministrador() != null) {
            dto.setMatriculaAdministrador(user.getMatriculaAdministrador());
        }

        return dto;
    }

    private AppUserDTO mapToDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setIdAppUser(user.getIdAppUser());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setUsername(user.getUsername());
        dto.setMail(user.getMail());
        dto.setTelefono(user.getTelefono());
        dto.setMatriculaAdministrador(user.getMatriculaAdministrador());
        return dto;
    }
}
