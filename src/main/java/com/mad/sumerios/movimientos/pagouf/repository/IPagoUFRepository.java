package com.mad.sumerios.movimientos.pagouf.repository;

import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IPagoUFRepository extends JpaRepository<PagoUF, Long> {
    List<PagoUF> findByUnidadFuncional_idUf(Long idUf);
//    List<PagoUF> findByConsorcio_IdConsorcioAndFechaBetween(Long idConsorcio, Date fechaInicio, Date fechaFin);
}
