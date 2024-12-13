package com.mad.sumerios.estadocuentaufcopia.repository;

import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.estadocuentaufcopia.model.CopiaEstadoCuentaUf;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEstadoCuentaUfCopiaRepository extends JpaRepository<CopiaEstadoCuentaUf, Long> {
    List<CopiaEstadoCuentaUf> findByIdUf(Long idUf);
}
