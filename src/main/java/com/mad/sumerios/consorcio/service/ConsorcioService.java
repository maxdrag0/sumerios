package com.mad.sumerios.consorcio.service;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.repository.IAdministracionRepository;
import com.mad.sumerios.consorcio.dto.*;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.estadocuentaconsorcio.dto.EstadoCuentaConsorcioCreateDTO;
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

        Consorcio consorcio = mapToConsorcioEntity(dto);
        consorcioRepository.save(consorcio);

        EstadoCuentaConsorcioCreateDTO ecDTO = new EstadoCuentaConsorcioCreateDTO(consorcio.getIdConsorcio());
        estadoCuentaConsorcioService.createEstadoCuenta(ecDTO);

        return consorcio;
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

        if (cons.getAdministracion().getIdAdm() != idAdm) {
            throw new Exception("El consorcio no pertenece a la administración especificada");
        }

        validarDireccionUnicaUpdate(dto.getDireccion(), idConsorcio);
        validarCuitUnicoUpdate(dto.getCuit(), idConsorcio);

        // Actualizar los datos del consorcio directamente desde el DTO
        cons.setNombre(dto.getNombre());
        cons.setDireccion(dto.getDireccion());
        cons.setCiudad(dto.getCiudad());
        cons.setCuit(dto.getCuit());

        cons.setTitulo(dto.getTitulo());
        cons.setCbu(dto.getCbu());
        cons.setBanco(dto.getBanco());
        cons.setNumCuenta(dto.getNumCuenta());
        cons.setAlias(dto.getAlias());
        cons.setPorcentajeIntereses(dto.getPorcentajeIntereses());
        cons.setSegundoVencimiento(dto.getSegundoVencimiento());
        cons.setPorcentajeSegundoVencimiento(dto.getPorcentajeSegundoVencimiento());

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


    public void ajustarInteresesSegundoVencimientoConsorcio(Long idConsorcio,
                                                            Double porcentajeIntereses,
                                                            Boolean segundoVencimiento,
                                                            Double porcentajeSegundoVencimiento) throws Exception{
        Consorcio cons = consorcioRepository.findById(idConsorcio)
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));

        cons.setPorcentajeIntereses(porcentajeIntereses);
        cons.setSegundoVencimiento(segundoVencimiento);
        cons.setPorcentajeSegundoVencimiento(porcentajeSegundoVencimiento);
    }

//  VALIDACIONES CREATE
    private String nullSafe(String valor) {
        return (valor == null) ? "" : valor;
    }
    private void validarDireccionUnicaCreate(String direccion) throws Exception {
    if (consorcioRepository.findByDireccion(direccion).isPresent()) {
        throw new Exception("El consorcio ya está registrant. La dirección: " + direccion + " ya existe");
    }
}
    private void validarCuitUnicoCreate(String cuit) throws Exception {
        if(cuit!=null && !cuit.isBlank()){
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
    private void validarCuitUnicoUpdate(String cuit, Long idActualConsorcio) throws Exception {
        if(cuit != null && !cuit.isEmpty()){
            if (consorcioRepository.findByCuit(cuit)
                    .filter(c -> !Long.valueOf(c.getCuit()).equals(idActualConsorcio))  // Conversión de long a Long
                    .isPresent()) {
                throw new Exception("El consorcio ya está registrado. El CUIT: " + cuit + " ya existe");
            }
        }
    }

//  MAPEO DTO
    private Consorcio mapToConsorcioEntity(ConsorcioCreateDTO dto) throws Exception {
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

        consorcio.setPorcentajeIntereses(dto.getPorcentajeIntereses());
        consorcio.setSegundoVencimiento(dto.getSegundoVencimiento());
        consorcio.setPorcentajeSegundoVencimiento(dto.getPorcentajeSegundoVencimiento());

        return consorcio;
    }
    private ConsorcioResponseDTO mapToConsorcioResponseDTO(Consorcio consorcio) {
        ConsorcioResponseDTO consorcioDTO = new ConsorcioResponseDTO();

        consorcioDTO.setIdConsorcio(consorcio.getIdConsorcio());
        consorcioDTO.setNombre(consorcio.getNombre());
        consorcioDTO.setDireccion(consorcio.getDireccion());
        consorcioDTO.setCiudad(consorcio.getCiudad());
        consorcioDTO.setBanco(consorcio.getBanco());

        // Usamos null-safe setters
        consorcioDTO.setCuit(nullSafe(consorcio.getCuit()));
        consorcioDTO.setTitulo(nullSafe(consorcio.getTitulo()));
        consorcioDTO.setCbu(nullSafe(consorcio.getCbu()));
        consorcioDTO.setNumCuenta(nullSafe(consorcio.getNumCuenta()));
        consorcioDTO.setAlias(nullSafe(consorcio.getAlias()));

        consorcioDTO.setPorcentajeIntereses(consorcio.getPorcentajeIntereses());
        consorcioDTO.setSegundoVencimiento(consorcio.getSegundoVencimiento());
        consorcioDTO.setPorcentajeSegundoVencimiento(consorcio.getPorcentajeSegundoVencimiento());

        // Datos de la administración
        consorcioDTO.setIdAdm(consorcio.getAdministracion().getIdAdm());

        // Estado de cuenta
        EstadoCuentaConsorcio ec = consorcio.getEstadoCuentaConsorcio();
        if (ec != null) {
            EstadoCuentaConsorcioDTO ecDto = new EstadoCuentaConsorcioDTO();
            ecDto.setIdEstadoCuentaConsorcio(ec.getIdEstadoCuentaConsorcio());
            ecDto.setTotal(ec.getTotal());
            ecDto.setEfectivo(ec.getEfectivo());
            ecDto.setBanco(ec.getBanco());
            ecDto.setFondoAdm(ec.getFondoAdm());
            consorcioDTO.setEstadoCuentaConsorcioDTO(ecDto);
        }

        return consorcioDTO;
    }


}
