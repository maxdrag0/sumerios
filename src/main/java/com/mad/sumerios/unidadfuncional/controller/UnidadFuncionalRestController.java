package com.mad.sumerios.unidadfuncional.controller;

import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import com.mad.sumerios.unidadfuncional.service.UnidadFuncionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidad_funcional/")
public class UnidadFuncionalRestController {

    private final UnidadFuncionalService unidadFuncionalService;

    @Autowired
    public UnidadFuncionalRestController(UnidadFuncionalService unidadFuncionalService) {
        this.unidadFuncionalService = unidadFuncionalService;
    }

    //  CREAR uf
    @PostMapping(value = "create", headers = "Accept=application/json")
    public ResponseEntity<String> createUnidadFuncional(@RequestBody UnidadFuncional unidadFuncional) {
        try{
            unidadFuncionalService.createUnidadFuncional(unidadFuncional);
            return ResponseEntity.status(HttpStatus.CREATED).body("Unidad Funcional creada exitosamente");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  LISTAR UFs
    @GetMapping(value = "get", headers = "Accept=application/json")
    public ResponseEntity<List<UnidadFuncional>> getAllUnidadFuncional() {
        try {
            List<UnidadFuncional> unidades = unidadFuncionalService.getUnidades();
            if (unidades.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(unidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = "get/{id}", headers = "Accept=application/json")
    public ResponseEntity<List<UnidadFuncional>> getUnidadFuncionalByIdConsorcio(@PathVariable Long id) {
        try {
            List<UnidadFuncional> unidades = unidadFuncionalService.getUnidadesFuncionalesPorConsorcio(id);
            if (unidades.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(unidades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    //  ACTUALIZAR UF
    @PutMapping (value = "update", headers = "Accept=application/json")
    public ResponseEntity<String> updateUnidadFuncional(@RequestBody UnidadFuncional unidadFuncional){
        try{
            unidadFuncionalService.updateUnidadFuncional(unidadFuncional);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Unidad Funcional modificada exitosamente"
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //  ELIMINAR UF
    @DeleteMapping (value = "delete/{id}", headers = "Accept=application/json")
    public ResponseEntity<String> deleteUnidadFuncional(@PathVariable Long id){
        try{
            UnidadFuncional uf = unidadFuncionalService.deleteUnidadFuncional(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Unidad Funcional eliminada exitosamente"
                    );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
