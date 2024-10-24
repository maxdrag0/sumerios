package com.mad.sumerios.proveedor.service;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.proveedor.dto.ProveedorCreateDTO;
import com.mad.sumerios.proveedor.dto.ProveedorResponseDTO;
import com.mad.sumerios.proveedor.model.Proveedor;
import com.mad.sumerios.proveedor.repository.IProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    private final IProveedorRepository proveedorRepository;

    @Autowired
    public ProveedorService (IProveedorRepository proveedorRepository){
        this.proveedorRepository = proveedorRepository;
    }

    // create proveedor
    public ProveedorResponseDTO createProveedor (ProveedorCreateDTO dto) throws Exception {
        validateTelefono(dto.getTelefono());
        validateCuit(dto.getCuit());

        Proveedor proveedor = mapToProveedorEntity(dto);
        proveedorRepository.save(proveedor);

        return mapToResponseDTO(proveedor);
    }

    // get all proveedores
    public List<ProveedorResponseDTO> getAllProveedores(){
        List<Proveedor> proveedores = proveedorRepository.findAll();
        return proveedores.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // delete proveedores
    public ProveedorResponseDTO deleteProveedor (Long idProveedor) throws Exception {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado con el ID: " + idProveedor));

        ProveedorResponseDTO proveedorDTO = mapToResponseDTO(proveedor);
        proveedorRepository.delete(proveedor);

        return proveedorDTO;
    }

    // validaciones
    private void validateTelefono(String telefono) throws Exception {
        Proveedor proveedor = proveedorRepository.findByTelefono(telefono);
        if(proveedor != null) {
            throw new Exception("El proveedor ya está registrado (telefono ya existente)");
        }
    }
    private void validateCuit(String cuit) throws Exception {
        Proveedor proveedor = proveedorRepository.findByCuit(cuit);
        if(proveedor != null) {
            throw new Exception("El proveedor ya está registrado (CUIT ya existente)");
        }
    }

    // mapeo a dto
    private ProveedorResponseDTO mapToResponseDTO(Proveedor proveedor) {
        ProveedorResponseDTO dto = new ProveedorResponseDTO();

        dto.setIdProveedor(proveedor.getIdProveedor());
        dto.setNombre(proveedor.getNombre());
        dto.setDescripcion(proveedor.getDescripcion());
        dto.setCuit(proveedor.getCuit());

        return dto;
    }
    private Proveedor mapToProveedorEntity(ProveedorCreateDTO dto) {
        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(dto.getNombre());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDescripcion(dto.getDescripcion());
        proveedor.setCuit(dto.getCuit());
        proveedor.setCbu(dto.getCbu());

        return proveedor;
    }
}
