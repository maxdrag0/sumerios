package com.mad.sumerios.unidadfuncional.controller;

import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.service.UnidadFuncionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consorcios/{idConsorcio}/unidades_funcionales")
public class UnidadFuncionalRestController {

    private final UnidadFuncionalService unidadFuncionalService;

    @Autowired
    public UnidadFuncionalRestController(UnidadFuncionalService unidadFuncionalService) {
        this.unidadFuncionalService = unidadFuncionalService;
    }

    //  CREAR uf
    @PostMapping
    public ResponseEntity<String> createUnidadFuncional(@PathVariable Long idConsorcio,
                                                        @RequestBody UnidadFuncional unidadFuncional) {
        try{
            unidadFuncionalService.createUnidadFuncional(idConsorcio, unidadFuncional);
            return ResponseEntity.status(HttpStatus.CREATED).body("Unidad Funcional creada exitosamente");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR UFs
    @GetMapping
    public ResponseEntity<List<UnidadFuncional>> getUnidadesFuncionales(@PathVariable Long idConsorcio) {
        try {
            List<UnidadFuncional> unidades = unidadFuncionalService.getUnidadesPorConsorcio(idConsorcio);
            if (unidades.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(unidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //  ACTUALIZAR UF
    @PutMapping("/{idUnidadFuncional}")
    public ResponseEntity<String> updateUnidadFuncional(@PathVariable Long idConsorcio,
                                                        @PathVariable Long idUnidadFuncional,
                                                        @RequestBody UnidadFuncional unidadFuncional) {
        try {
            // Asegúrate de que la unidad funcional a actualizar esté asociada con el consorcio correcto
            unidadFuncionalService.updateUnidadFuncional(idConsorcio, idUnidadFuncional, unidadFuncional);
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
