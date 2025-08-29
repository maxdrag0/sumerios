package com.mad.sumerios.estadocuentaconsorcio.controller;

import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioCreateDTO;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioDTO;
import com.mad.sumerios.estadocuentaconsorcio.service.EstadoCuentaConsorcioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estado_cuenta_consorcio")
public class EstadoCuentaConsorcioController {

    private final EstadoCuentaConsorcioService estadoCuentaConsorcioService;

    @Autowired
    public EstadoCuentaConsorcioController(EstadoCuentaConsorcioService estadoCuentaConsorcioService){
        this.estadoCuentaConsorcioService = estadoCuentaConsorcioService;
    }

    //CRUD
    //Create
    @PostMapping
    public ResponseEntity<String> createEstadoCuenta(@RequestBody EstadoCuentaConsorcioCreateDTO dto) {
        try{
            estadoCuentaConsorcioService.createEstadoCuenta(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Estado de cuenta creado exitosamente");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //Get
    //all
    @GetMapping("/all")
    public ResponseEntity<?> getAllEstadoCuenta() {
        try {
            List<EstadoCuentaConsorcioDTO> eas = estadoCuentaConsorcioService.getAllEstadoCuenta();
            if (eas.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No se encontraron estados de cuenta.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            return ResponseEntity.ok(eas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //Consorcio
    @GetMapping("/consorcio/{idConsorcio}")
    public ResponseEntity<?> getEstadoCuentaByConsorcio(@PathVariable Long idConsorcio){
        try{
            EstadoCuentaConsorcioDTO dto = estadoCuentaConsorcioService.getByIdConsorcio(idConsorcio);
            return ResponseEntity.ok(dto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    //Put
    @PutMapping("/{idEstadoCuenta}")
    public ResponseEntity<String> updateEgreso (@PathVariable Long idEstadoCuenta,
                                                @RequestBody EstadoCuentaConsorcioDTO dto){
        try{
            estadoCuentaConsorcioService.updateEstadoCuenta(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Estado de cuenta modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
