package com.mad.sumerios.estadocuentaconsorcio.repository;

import com.mad.sumerios.estadocuentaconsorcio.model.EstadoCuentaConsorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoCuentaConsorcioRepository extends JpaRepository<EstadoCuentaConsorcio, Long> {
    EstadoCuentaConsorcio findByConsorcio_idConsorcio(Long idConsorcio);
}
