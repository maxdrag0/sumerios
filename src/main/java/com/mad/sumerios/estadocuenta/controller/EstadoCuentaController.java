package com.mad.sumerios.estadocuenta.controller;

import com.mad.sumerios.estadocuenta.dto.EstadoCuentaCreateDTO;
import com.mad.sumerios.estadocuenta.dto.EstadoCuentaDTO;
import com.mad.sumerios.estadocuenta.service.EstadoCuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estado_cuenta")
public class EstadoCuentaController {

    private final EstadoCuentaService estadoCuentaService;

    @Autowired
    public EstadoCuentaController (EstadoCuentaService estadoCuentaService){
        this.estadoCuentaService = estadoCuentaService;
    }

    //CRUD
    //Create
    @PostMapping
    public ResponseEntity<String> createEstadoCuenta(@RequestBody EstadoCuentaCreateDTO dto) {
        try{
            estadoCuentaService.createEstadoCuenta(dto);
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
            List<EstadoCuentaDTO> eas = estadoCuentaService.getAllEstadoCuenta();
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
            EstadoCuentaDTO dto = estadoCuentaService.getByIdConsorcio(idConsorcio);
            return ResponseEntity.ok(dto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    //Put
    @PutMapping("/{idEstadoCuenta}")
    public ResponseEntity<String> updateEgreso (@PathVariable Long idEstadoCuenta,
                                                @RequestBody EstadoCuentaDTO dto){
        try{
            estadoCuentaService.updateEstadoCuenta(idEstadoCuenta, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Estado de cuenta modificado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
