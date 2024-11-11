package com.mad.sumerios.estadocuenta.repository;

import com.mad.sumerios.estadocuenta.model.EstadoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoCuentaRepository extends JpaRepository<EstadoCuenta, Long> {
    EstadoCuenta findByConsorcio_idConsorcio(Long idConsorcio);
}
