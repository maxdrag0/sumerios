package com.mad.sumerios.estadocuentauf.controller;

import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfCreateDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfUpdatePeriodo;
import com.mad.sumerios.estadocuentauf.service.EstadoCuentaUfService;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/estado_cuenta_uf")
public class EstadoCuentaUfController {

    private final EstadoCuentaUfService estadoCuentaUfService;

    @Autowired
    public EstadoCuentaUfController (EstadoCuentaUfService estadoCuentaUfService){
        this.estadoCuentaUfService = estadoCuentaUfService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<String> createEstadoCuentaUf(@RequestBody EstadoCuentaUfCreateDTO dto){
        try {
            estadoCuentaUfService.createEstadoCuentaUf(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estado de cuenta creado exitosamente");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/{idEstadoCuentaUf}")
    public ResponseEntity<String> updateEstadoCuentaUf(@PathVariable Long idEstadoCuentaUf,
                                                       @RequestBody EstadoCuentaUfDTO dto){
        try{
            estadoCuentaUfService.updateEstadoCuentaUf(idEstadoCuentaUf, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Estado de cuenta modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update_periodo")
    public ResponseEntity<String> updatePeriodos(@RequestBody EstadoCuentaUfUpdatePeriodo dto){
        try{
            estadoCuentaUfService.updatePeriodos(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Periodos de cuentas modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET BY UF
    @GetMapping("/uf/{idUf}")
    public ResponseEntity<?> getEstadoCuentaUfByUf(@PathVariable Long idUf){
        try{
            EstadoCuentaUfDTO dto = estadoCuentaUfService.getEstadoCuentaByUf(idUf);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
