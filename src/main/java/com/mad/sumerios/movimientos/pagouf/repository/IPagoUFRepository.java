package com.mad.sumerios.movimientos.pagouf.repository;

import com.mad.sumerios.enums.FormaPago;
import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface IPagoUFRepository extends JpaRepository<PagoUF, Long> {
    List<PagoUF> findByUnidadFuncional_idUf(Long idUf);
    List<PagoUF> findByUnidadFuncional_idUfAndFechaBetween(Long idUf, LocalDate startDate, LocalDate endDate);
    List<PagoUF> findByIdConsorcio(Long idConsorcio);
    List<PagoUF> findByIdConsorcioAndFechaBetween(Long idConsorcio, LocalDate startDate, LocalDate endDate);
    List<PagoUF> findByIdConsorcioAndFormaPago(Long idConsorcio, FormaPago formaPago);
    List<PagoUF> findByFormaPago (FormaPago formaPago);
//    List<PagoUF> findByExpensa_idExpensa(Long idExpensa);
}
