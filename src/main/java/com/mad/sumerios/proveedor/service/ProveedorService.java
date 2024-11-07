package com.mad.sumerios.proveedor.service;

import com.mad.sumerios.proveedor.dto.ProveedorCreateDTO;
import com.mad.sumerios.proveedor.dto.ProveedorResponseDTO;
import com.mad.sumerios.proveedor.dto.ProveedorUpdateDTO;
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
    // get proveedor by id
    public ProveedorResponseDTO getProvedorById(Long idProveedor) throws Exception {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado con el ID: "+ idProveedor));
        return mapToResponseDTO(proveedor);
    }
    // delete proveedores
    public ProveedorResponseDTO deleteProveedor (Long idProveedor) throws Exception {
        Proveedor proveedor = proveedorRepository.findById(idProveedor)
                .orElseThrow(() -> new Exception("Proveedor no encontrado con el ID: " + idProveedor));

        ProveedorResponseDTO proveedorDTO = mapToResponseDTO(proveedor);
        proveedorRepository.delete(proveedor);

        return proveedorDTO;
    }
    // update proveedor
    public ProveedorResponseDTO updateProveedor (ProveedorUpdateDTO dto) throws Exception{
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new Exception ("Proveedor no encontrado"));

        validateCuitUpdate(dto.getIdProveedor(), dto.getCuit());
        validateTelefonoUpdate(dto.getIdProveedor(), dto.getTelefono());
        Proveedor proveedorUpdated = mapToProveedorEntityUpdate(dto);

        //
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setNombre(dto.getNombre());
        proveedor.setDescripcion(dto.getDescripcion());
        proveedor.setCbu(dto.getCbu());
        proveedor.setCuit(dto.getCuit());

        proveedorRepository.save(proveedor);

        return mapToResponseDTO(proveedor);
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

    // validaciones update
    private void validateTelefonoUpdate(Long idProveedor, String telefono) throws Exception {
        Proveedor proveedor = proveedorRepository.findByTelefono(telefono);
        if(proveedor.getIdProveedor() != idProveedor) {
            throw new Exception("El telefono ya está registrado en otro proveedor existente)");
        }
    }
    private void validateCuitUpdate(Long idProveedor, String cuit) throws Exception {
        Proveedor proveedor = proveedorRepository.findByCuit(cuit);
        if(proveedor.getIdProveedor() != idProveedor) {
            throw new Exception("El CUIT ya está registrado en otro proveedor existente)");
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

    // mapeo dto a entity
    private Proveedor mapToProveedorEntity(ProveedorCreateDTO dto) {
        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(dto.getNombre());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDescripcion(dto.getDescripcion());
        proveedor.setCuit(dto.getCuit());
        proveedor.setCbu(dto.getCbu());

        return proveedor;
    }
    private Proveedor mapToProveedorEntityUpdate(ProveedorUpdateDTO dto) {
        Proveedor proveedor = new Proveedor();

        proveedor.setIdProveedor(dto.getIdProveedor());
        proveedor.setNombre(dto.getNombre());
        proveedor.setDescripcion(dto.getDescripcion());
        proveedor.setCbu(dto.getCbu());
        proveedor.setCuit(dto.getCuit());
        proveedor.setTelefono(dto.getTelefono());

        return proveedor;
    }


}
