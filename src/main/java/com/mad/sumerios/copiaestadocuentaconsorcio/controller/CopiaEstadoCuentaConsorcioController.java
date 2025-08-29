package com.mad.sumerios.copiaestadocuentaconsorcio.controller;

import com.mad.sumerios.copiaestadocuentaconsorcio.dto.CopiaEstadoCuentaConsorcioDTO;
import com.mad.sumerios.copiaestadocuentaconsorcio.service.CopiaEstadoCuentaConsorcioService;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioDTO;
import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/copia_estado_cuenta_consorcio")
public class CopiaEstadoCuentaConsorcioController {

    private final CopiaEstadoCuentaConsorcioService copiaEstadoCuentaConsorcioService;

    @Autowired
    public CopiaEstadoCuentaConsorcioController(CopiaEstadoCuentaConsorcioService copiaEstadoCuentaConsorcioService){
        this.copiaEstadoCuentaConsorcioService = copiaEstadoCuentaConsorcioService;
    }

    @PostMapping
    public ResponseEntity<?> createCopiaEstadoCuentaConsorcio (@RequestBody EstadoCuentaConsorcioDTO estadoCuentaConsorcio,
                                                               @RequestBody YearMonth periodo){
        try{
            copiaEstadoCuentaConsorcioService.createCopiaEstadoCuentaConsorcio(estadoCuentaConsorcio, periodo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Copia del cuenta del CONSORCIO creado exitosamente");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/consorcios/{idConsorcio}/periodo/{periodo}")
    public ResponseEntity<?> getByConsorcioAndPeriodo (@PathVariable Long idConsorcio,
                                                       @PathVariable YearMonth periodo){
        try{
            CopiaEstadoCuentaConsorcioDTO dto = copiaEstadoCuentaConsorcioService.getByConsorcioAndPeriodo(idConsorcio, periodo);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
