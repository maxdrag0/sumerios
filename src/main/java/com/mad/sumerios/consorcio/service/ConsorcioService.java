package com.mad.sumerios.consorcio.service;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.repository.IAdministracionRepository;
import com.mad.sumerios.consorcio.dto.*;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioDTO;
import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import com.mad.sumerios.estadocuentaconsorcio.service.EstadoCuentaConsorcioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsorcioService {

    private final IConsorcioRepository consorcioRepository;
    private final IAdministracionRepository administracionRepository;
    private final EstadoCuentaConsorcioService estadoCuentaConsorcioService;

    @Autowired
    public ConsorcioService(IConsorcioRepository consorcioRepository,
                            IAdministracionRepository administracionRepository,
                            EstadoCuentaConsorcioService estadoCuentaConsorcioService) {
        this.consorcioRepository = consorcioRepository;
        this.administracionRepository = administracionRepository;
        this.estadoCuentaConsorcioService = estadoCuentaConsorcioService;
    }

    //  CREAR CONSORCIO
    public Consorcio createConsorcio(Long idAdm, ConsorcioCreateDTO dto) throws Exception {
        if(administracionRepository.findByIdAdm(idAdm).isEmpty()){
            throw new Exception("Administración no encontrada");
        }

        return consorcioRepository.save(mapToConsorcioEntity(dto));
    }

    //  LISTAR CONSORCIOS
    // Obtener consorcios por administración
    public List<ConsorcioResponseDTO> getConsorciosPorAdministracion(Long idAdm) {
        List<Consorcio> consorcios = consorcioRepository.findByAdministracion_IdAdm(idAdm);
        return consorcios.stream().map(this::mapToConsorcioResponseDTO).collect(Collectors.toList());
    }

    // Obtener consorcio por ID
    public ConsorcioResponseDTO getConsorcioById(Long idConsorcio) throws Exception {
        Consorcio consorcio = consorcioRepository.findById(idConsorcio)
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));
        return mapToConsorcioResponseDTO(consorcio);
    }

    //  ACTUALIZAR CONSORCIO
    public void updateConsorcio(Long idAdm, Long idConsorcio, ConsorcioUpdateDTO dto) throws Exception {
        Consorcio cons = consorcioRepository.findById(idConsorcio)
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));

        if(cons.getAdministracion().getIdAdm() != idAdm){
            throw new Exception("El consorcio no pertenece a la administración especificada");
        }
        // Validar si el nombre o la dirección ya están en uso por otro consorcio
        validarNombreUnicoUpdate(dto.getNombre(), idConsorcio);
        validarDireccionUnicaUpdate(dto.getDireccion(), idConsorcio);
        validarCuitUnicoUpdate(dto.getCuit(), idConsorcio);

        Consorcio consUpdate = mapToConsorcioEntityUpdate(dto);

        // Actualizar los datos del consorcio
        cons.setNombre(consUpdate.getNombre());
        cons.setDireccion(consUpdate.getDireccion());
        cons.setCiudad(consUpdate.getCiudad());
        cons.setCuit(consUpdate.getCuit());

        // Actualizar los datos bancarios del consorcio
        cons.setTitulo(consUpdate.getTitulo());
        cons.setCbu(consUpdate.getCbu());
        cons.setBanco(consUpdate.getBanco());
        cons.setNumCuenta(consUpdate.getNumCuenta());
        cons.setAlias(consUpdate.getAlias());

        // Guardar los cambios
        consorcioRepository.save(cons);
    }

    //  ELIMINAR CONSORCIO
    public void deleteConsorcio(Long idAdm, Long idConsorcio) {
        Optional<Consorcio> consorcioOpt = consorcioRepository.findByidConsorcioAndAdministracion_IdAdm(idConsorcio, idAdm);

        if (consorcioOpt.isPresent()) {
            consorcioRepository.deleteById(idConsorcio);  // Elimina el consorcio
        } else {
            throw new RuntimeException("Consorcio no encontrado o no pertenece a la administración");
        }
    }

//  VALIDACIONES CREATE
    private void validarDireccionUnicaCreate(String direccion) throws Exception {
    if (consorcioRepository.findByDireccion(direccion).isPresent()) {
        throw new Exception("El consorcio ya está registrant. La dirección: " + direccion + " ya existe");
    }
}
    private void validarNombreUnicoCreate(String nombre) throws Exception {
        if (consorcioRepository.findByNombre(nombre).isPresent()) {
            throw new Exception("El consorcio ya está registrado. El nombre: " + nombre + " ya existe");
        }
    }
    private void validarCuitUnicoCreate(String cuit) throws Exception {
        if(cuit!=null){
            if (consorcioRepository.findByCuit(cuit).isPresent()) {
                throw new Exception("El consorcio ya está registrado. El CUIT: " + cuit + " ya existe");
            }
        }
    }

//  VALIDACIONES UPDATE
    private void validarDireccionUnicaUpdate(String direccion, Long idActualConsorcio) throws Exception {
        if (consorcioRepository.findByDireccion(direccion)
                .filter(c -> !Long.valueOf(c.getIdConsorcio()).equals(idActualConsorcio))  // Conversión de long a Long
                .isPresent()) {
            throw new Exception("El consorcio ya está registrado. La dirección: " + direccion + " ya existe");
        }
    }
    private void validarNombreUnicoUpdate(String nombre, Long idActualConsorcio) throws Exception {
        if (consorcioRepository.findByNombre(nombre)
                .filter(c -> !Long.valueOf(c.getIdConsorcio()).equals(idActualConsorcio))  // Conversión de long a Long
                .isPresent()) {
            throw new Exception("El consorcio ya está registrado. El nombre: " + nombre + " ya existe");
        }
    }
    private void validarCuitUnicoUpdate(String cuit, Long idActualConsorcio) throws Exception {
        if (consorcioRepository.findByCuit(cuit)
                .filter(c -> !Long.valueOf(c.getCuit()).equals(idActualConsorcio))  // Conversión de long a Long
                .isPresent()) {
            throw new Exception("El consorcio ya está registrado. El CUIT: " + cuit + " ya existe");
        }
    }

//  MAPEO DTO
    private Consorcio mapToConsorcioEntity(ConsorcioCreateDTO dto) throws Exception {
        validarNombreUnicoCreate(dto.getNombre());
        validarDireccionUnicaCreate(dto.getDireccion());
        validarCuitUnicoCreate(dto.getCuit());
        Optional<Administracion> adm = administracionRepository.findById(dto.getIdAdm());
        if (adm.isEmpty()) {
            throw new Exception("Administración no encontrada.");
        }

        Consorcio consorcio = new Consorcio();

        consorcio.setAdministracion(adm.get());
        consorcio.setNombre(dto.getNombre());
        consorcio.setDireccion(dto.getDireccion());
        consorcio.setCiudad(dto.getCiudad());
        consorcio.setCuit(dto.getCuit());
        consorcio.setTitulo(dto.getTitulo());
        consorcio.setCbu(dto.getCbu());
        consorcio.setBanco(dto.getBanco());
        consorcio.setNumCuenta(dto.getNumCuenta());
        consorcio.setAlias(dto.getAlias());

        return consorcio;
    }
    private ConsorcioResponseDTO mapToConsorcioResponseDTO(Consorcio consorcio) {
        // MAPEO DATOS CONSORCIO
        ConsorcioResponseDTO consorcioDTO = validarNullConsorcio(consorcio);
        consorcioDTO.setIdConsorcio(consorcio.getIdConsorcio());
        consorcioDTO.setNombre(consorcio.getNombre());
        consorcioDTO.setDireccion(consorcio.getDireccion());
        // FIN MAPEO

        // MAPEO DATOS ADMINISTRACION
        Administracion adm = consorcio.getAdministracion();
        ConsorcioAdmDTO admDTO = new ConsorcioAdmDTO();
        admDTO.setIdAdm(adm.getIdAdm());
        admDTO.setNombre(adm.getNombre());
        consorcioDTO.setAdministracion(admDTO);
        // FIN MAPEO

        // MAPEO DATOs UFs
        List<ConsorcioUfDTO> ufDTOList = consorcio.getUnidadesFuncionales().stream()
                .map(uf ->{
                    ConsorcioUfDTO ufDTO = new ConsorcioUfDTO();
                    ufDTO.setIdUf(uf.getIdUf());
                    ufDTO.setUnidadFuncional(uf.getUnidadFuncional());
                    ufDTO.setTitulo(uf.getTitulo());
                    ufDTO.setApellidoPropietario(uf.getApellidoPropietario());
                    ufDTO.setNombrePropietario(uf.getNombrePropietario());

                    return ufDTO;
                }).collect(Collectors.toList());
        consorcioDTO.setUnidades(ufDTOList);
        // FIN MAPEO

        // MAPEO ESTADO CUENTA
        EstadoCuentaConsorcio ec = consorcio.getEstadoCuentaConsorcio();
        EstadoCuentaConsorcioDTO ecDto = new EstadoCuentaConsorcioDTO();
        ecDto.setIdEstadoCuentaConsorcio(ec.getIdEstadoCuentaConsorcio());
        ecDto.setTotal(ec.getTotal());
        ecDto.setEfectivo(ec.getEfectivo());
        ecDto.setBanco(ec.getBanco());
        ecDto.setFondoAdm(ec.getFondoAdm());
        consorcioDTO.setEstadoCuentaConsorcioDTO(ecDto);
        // FIN MAPEO

        return consorcioDTO;
    }
    private Consorcio mapToConsorcioEntityUpdate(ConsorcioUpdateDTO dto) throws Exception {
        Consorcio consorcio = new Consorcio();
        Optional<Administracion> adm = administracionRepository.findById(dto.getIdAdm());
        if (adm.isEmpty()) {
            throw new Exception("Administración no encontrada.");
        }

        consorcio.setAdministracion(adm.get());
        consorcio.setIdConsorcio(dto.getIdConsorcio());
        consorcio.setNombre(dto.getNombre());
        consorcio.setDireccion(dto.getDireccion());
        consorcio.setCiudad(dto.getCiudad());
        consorcio.setCuit(dto.getCuit());
        consorcio.setTitulo(dto.getTitulo());
        consorcio.setCbu(dto.getCbu());
        consorcio.setBanco(dto.getBanco());
        consorcio.setNumCuenta(dto.getNumCuenta());
        consorcio.setAlias(dto.getAlias());

        return consorcio;
    }

    private ConsorcioResponseDTO validarNullConsorcio(Consorcio consorcio){
        ConsorcioResponseDTO dto = new ConsorcioResponseDTO();
        if (consorcio.getCuit() == null) {
            dto.setCuit("");
        } else {
            dto.setCuit(consorcio.getCuit());
        }

        if (consorcio.getTitulo() == null) {
            dto.setTitulo("");
        } else {
            dto.setTitulo(consorcio.getTitulo());
        }

        if (consorcio.getCbu() == null) {
            dto.setCbu("");
        } else {
            dto.setCbu(consorcio.getCbu());
        }

        if (consorcio.getNumCuenta() == null) {
            dto.setNumCuenta("");
        } else {
            dto.setNumCuenta(consorcio.getNumCuenta());
        }

        if (consorcio.getAlias() == null) {
            dto.setAlias("");
        } else {
            dto.setAlias(consorcio.getAlias());
        }


        return dto;
    }
}
