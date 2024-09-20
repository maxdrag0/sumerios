package com.mad.sumerios.consorcio.service;

import com.mad.sumerios.administracion.model.Administracion;
import com.mad.sumerios.administracion.repository.IAdministracionRepository;
import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsorcioService {

    private final IConsorcioRepository consorcioRepository;
    private final IAdministracionRepository administracionRepository;

    @Autowired
    public ConsorcioService(IConsorcioRepository consorcioRepository, IAdministracionRepository administracionRepository) {
        this.consorcioRepository = consorcioRepository;
        this.administracionRepository = administracionRepository;
    }

    //  CREAR CONSORCIO
    public Consorcio createConsorcio(Long idAdm, Consorcio consorcio) throws Exception {
        validarNombreUnicoCreate(consorcio.getNombre());
        validarDireccionUnicaCreate(consorcio.getDireccion());

        Optional<Administracion> adm = administracionRepository.findById(idAdm);
        if (!adm.isPresent()) {
            throw new Exception("Administración no encontrada.");
        }

        consorcio.setAdministracion(adm.get());
        consorcioRepository.save(consorcio);

        return consorcio;
    }

    //  LISTAR CONSORCIOS
    // Obtener consorcios por administración
    public List<Consorcio> getConsorciosPorAdministracion(Long idAdm) {
        return consorcioRepository.findByAdministracion_IdAdm(idAdm);
    }

    //  ACTUALIZAR CONSORCIO
    public void updateConsorcio(Long idAdm, Long idConsorcio, Consorcio consorcio) throws Exception {
        // Verificar que la administración existe
        Optional<Administracion> administracion = administracionRepository.findById(idAdm);
        if (!administracion.isPresent()) {
            throw new Exception("Administración no encontrada");
        }

        // Buscar el consorcio a actualizar
        Consorcio cons = consorcioRepository.findById(idConsorcio)
                .orElseThrow(() -> new Exception("Consorcio no encontrado"));

        // Validar si el nombre o la dirección ya están en uso por otro consorcio
        validarNombreUnicoUpdate(consorcio.getNombre(), idConsorcio);
        validarDireccionUnicaUpdate(consorcio.getDireccion(), idConsorcio);

        // Actualizar los datos del consorcio
        cons.setNombre(consorcio.getNombre());
        cons.setDireccion(consorcio.getDireccion());

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

//  VALIDACIONES
    private void validarNombreUnicoUpdate(String nombre, Long idActualConsorcio) throws Exception {
        if (consorcioRepository.findByNombre(nombre)
                .filter(c -> !Long.valueOf(c.getIdConsorcio()).equals(idActualConsorcio))  // Conversión de long a Long
                .isPresent()) {
            throw new Exception("El consorcio ya está registrado. El nombre: " + nombre + " ya existe");
        }
    }

    private void validarNombreUnicoCreate(String nombre) throws Exception {
        if (consorcioRepository.findByNombre(nombre).isPresent()) {
            throw new Exception("El consorcio ya está registrado. El nombre: " + nombre + " ya existe");
        }
    }

    private void validarDireccionUnicaUpdate(String direccion, Long idActualConsorcio) throws Exception {
        if (consorcioRepository.findByDireccion(direccion)
                .filter(c -> !Long.valueOf(c.getIdConsorcio()).equals(idActualConsorcio))  // Conversión de long a Long
                .isPresent()) {
            throw new Exception("El consorcio ya está registrado. La dirección: " + direccion + " ya existe");
        }
    }

    private void validarDireccionUnicaCreate(String direccion) throws Exception {
        if (consorcioRepository.findByDireccion(direccion).isPresent()) {
            throw new Exception("El consorcio ya está registrado. La dirección: " + direccion + " ya existe");
        }
    }
}
