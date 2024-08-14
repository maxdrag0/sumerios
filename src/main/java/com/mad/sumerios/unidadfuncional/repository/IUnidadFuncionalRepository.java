package com.mad.sumerios.unidadfuncional.repository;


import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUnidadFuncionalRepository extends JpaRepository<UnidadFuncional, Long> {
    Optional<UnidadFuncional> findByNumeroYLetraEnConsorcio(Consorcio consorcio, String numeroUnidad, String letraUnidad);
    List<UnidadFuncional> findByConsorcio(Consorcio consorcio);
}
