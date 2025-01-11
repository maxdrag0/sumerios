package com.mad.sumerios.copiaestadocuentauf.repository;

import com.mad.sumerios.copiaestadocuentauf.model.CopiaEstadoCuentaUf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface IEstadoCuentaUfCopiaRepository extends JpaRepository<CopiaEstadoCuentaUf, Long> {
    List<CopiaEstadoCuentaUf> findByIdUf(Long idUf);
    CopiaEstadoCuentaUf findByIdEstadoCuentaUfAndPeriodo(Long idEstadoCuentaUf, YearMonth periodo);
}
