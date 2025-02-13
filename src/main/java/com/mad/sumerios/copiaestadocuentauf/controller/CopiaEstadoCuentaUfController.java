package com.mad.sumerios.copiaestadocuentauf.controller;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.copiaestadocuentauf.dto.CopiaEstadoCuentaUfDTO;
import com.mad.sumerios.copiaestadocuentauf.service.CopiaEstadoCuentaUfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/copia_estado_cuenta_uf")
public class CopiaEstadoCuentaUfController {

    private final CopiaEstadoCuentaUfService copiaEstadoCuentaUfService;

    @Autowired
    public CopiaEstadoCuentaUfController (CopiaEstadoCuentaUfService copiaEstadoCuentaUfService){
        this.copiaEstadoCuentaUfService = copiaEstadoCuentaUfService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<String> createCopiaEstadoCuentaUf(@RequestBody EstadoCuentaUfDTO dto){
        try {
            copiaEstadoCuentaUfService.createCopiaEstadoCuentaUf(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Copia del cuenta creado exitosamente");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/uf/{idUf}")
    public ResponseEntity<?> getEstadoCuentaUfByUf(@PathVariable Long idUf){
        try{
            List<CopiaEstadoCuentaUfDTO> dtos = copiaEstadoCuentaUfService.getCopiasEstadoCuentaUf(idUf);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
