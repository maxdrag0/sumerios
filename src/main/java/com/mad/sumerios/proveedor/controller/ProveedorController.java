package com.mad.sumerios.proveedor.controller;

import com.mad.sumerios.proveedor.dto.ProveedorCreateDTO;
import com.mad.sumerios.proveedor.dto.ProveedorResponseDTO;
import com.mad.sumerios.proveedor.dto.ProveedorUpdateDTO;
import com.mad.sumerios.proveedor.service.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administraciones/{idAdm}/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService){
        this.proveedorService = proveedorService;
    }

    @PostMapping
    public ResponseEntity<?> createProveedor(@RequestBody ProveedorCreateDTO dto) {
        try {
            // Llama al servicio y guarda el response DTO
            ProveedorResponseDTO responseDTO = proveedorService.createProveedor(dto);
            // Devuelve el response DTO con el estado 201 (CREATED)
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            // En caso de error, devuelve un estado 400 con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> getAllProveedores (@PathVariable Long idAdm){
        try {
            List<ProveedorResponseDTO> proveedores = proveedorService.getAllProveedores(idAdm);
            if (proveedores.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/{idProveedor}")
    public ResponseEntity<?> getEgresosByComprobante (@PathVariable Long idProveedor){
        try{
            ProveedorResponseDTO dto = proveedorService.getProvedorById(idProveedor);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
    @DeleteMapping("/{idProveedor}")
    public ResponseEntity<String> deleteProveedor(@PathVariable Long idProveedor){
        try{
            ProveedorResponseDTO proveedor = proveedorService.deleteProveedor(idProveedor);
            return ResponseEntity.ok("Proveedor "+proveedor.getNombre()+" eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar el proveedor: " + e.getMessage());
        }
    }
    @PutMapping("/{idProveedor}")
    public ResponseEntity<?> updateProveedor(@PathVariable Long idProveedor, @RequestBody ProveedorUpdateDTO dto){
        try {
            ProveedorResponseDTO responseDTO = proveedorService.updateProveedor(dto);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
