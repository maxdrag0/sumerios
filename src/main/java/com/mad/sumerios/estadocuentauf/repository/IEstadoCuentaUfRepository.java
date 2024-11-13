package com.mad.sumerios.estadocuentauf.repository;

import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoCuentaUfRepository extends JpaRepository<EstadoCuentaUf, Long> {
    EstadoCuentaUf findByUnidadFuncional_idUf(Long idUf);
}
