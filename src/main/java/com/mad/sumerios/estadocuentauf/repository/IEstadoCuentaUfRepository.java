package com.mad.sumerios.estadocuentauf.repository;

import com.mad.sumerios.estadocuentauf.model.EstadoCuentaUf;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEstadoCuentaUfRepository extends JpaRepository<EstadoCuentaUf, Long> {
    EstadoCuentaUf findByUnidadFuncional_idUf(Long idUf);
    List<EstadoCuentaUf> findByUnidadFuncionalIn(List<UnidadFuncional> unidadFuncionalList);
}
