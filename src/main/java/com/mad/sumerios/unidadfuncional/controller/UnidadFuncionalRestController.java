package com.mad.sumerios.unidadfuncional.controller;

import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalCreateDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalUpdateDTO;
import com.mad.sumerios.unidadfuncional.service.UnidadFuncionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/administraciones/{idAdm}/consorcios/{idConsorcio}/unidades_funcionales")
public class UnidadFuncionalRestController {

    private final UnidadFuncionalService unidadFuncionalService;

    @Autowired
    public UnidadFuncionalRestController(UnidadFuncionalService unidadFuncionalService) {
        this.unidadFuncionalService = unidadFuncionalService;
    }

    //  CREAR uf
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUnidadFuncional(@RequestBody UnidadFuncionalCreateDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try{
            UnidadFuncionalResponseDTO ufCreada = unidadFuncionalService.createUnidadFuncional(dto);
            response.put("id", ufCreada.getIdUf());
            response.put("message", "Consorcio creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e){
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/list")
    public ResponseEntity<String> createUnidadesFuncionales(@RequestBody List<UnidadFuncionalCreateDTO> dtos) {
        try{
            unidadFuncionalService.createUnidadesFuncionales(dtos);
            return ResponseEntity.status(HttpStatus.CREATED).body("Unidades Funcionales creadas exitosamente");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR UFs
    @GetMapping
    public ResponseEntity<?> getUnidadesFuncionales(@PathVariable Long idConsorcio) {
        try {
            List<UnidadFuncionalResponseDTO> unidades = unidadFuncionalService.getUnidadesPorConsorcio(idConsorcio);
            if (unidades.isEmpty()) {
                // Mensaje personalizado cuando no hay unidades funcionales
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No se encontraron unidades funcionales para este consorcio.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            return ResponseEntity.ok(unidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // get by id
    @GetMapping("/{idUnidadFuncional}")
    public ResponseEntity<?> getUnidadFuncionalById (@PathVariable Long idUf){
        try{
            UnidadFuncionalResponseDTO dto = unidadFuncionalService.getUnidadFuncionalById(idUf);
            return ResponseEntity.ok(dto);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    //  ACTUALIZAR UF
    @PutMapping("/{idUf}")
    public ResponseEntity<String> updateUnidadFuncional(@PathVariable Long idConsorcio,
                                                        @PathVariable Long idUf,
                                                        @RequestBody UnidadFuncionalUpdateDTO dto) {
        try {
            unidadFuncionalService.updateUnidadFuncional(idConsorcio, idUf, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Unidad Funcional modificada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR UF
    @DeleteMapping ("/{idUnidadFuncional}")
    public ResponseEntity<String> deleteUnidadFuncional(@PathVariable Long idConsorcio,
                                                        @PathVariable Long idUnidadFuncional){
        try{
            unidadFuncionalService.deleteUnidadFuncional(idConsorcio, idUnidadFuncional);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Unidad Funcional eliminada exitosamente"
                    );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
