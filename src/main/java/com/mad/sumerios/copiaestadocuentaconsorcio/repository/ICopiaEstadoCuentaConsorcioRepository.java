package com.mad.sumerios.copiaestadocuentaconsorcio.repository;

import com.mad.sumerios.copiaestadocuentaconsorcio.model.CopiaEstadoCuentaConsorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.time.YearMonth;

@Repository
public interface ICopiaEstadoCuentaConsorcioRepository extends JpaRepository<CopiaEstadoCuentaConsorcio, Long> {
    CopiaEstadoCuentaConsorcio findByIdConsorcioAndPeriodo (Long idConsorcio, YearMonth periodo);
}
