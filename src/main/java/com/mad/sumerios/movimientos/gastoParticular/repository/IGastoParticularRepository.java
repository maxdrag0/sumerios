package com.mad.sumerios.movimientos.gastoParticular.repository;

import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface IGastoParticularRepository extends JpaRepository<GastoParticular, Long> {
    List<GastoParticular> findByIdProveedor(Long idProveedor);
    List<GastoParticular> findByIdUf(Long idUf);
    List<GastoParticular> findByIdUfAndFechaBetween(Long idConsorcio, LocalDate startDate, LocalDate endDate);
    List<GastoParticular> findByIdConsorcio(Long idConsorcio);
    List<GastoParticular> findByPeriodoAndIdConsorcio(YearMonth periodo, Long idConsorcio);
    List<GastoParticular> findByIdConsorcioAndFechaBetween(Long idConsorcio, LocalDate startDate, LocalDate endDate);
    List<GastoParticular> findByTotalFinal(Double totalFinal);
    List<GastoParticular> findByExpensa_IdExpensa(Long idExpensa);
    GastoParticular findByComprobante(String comprobante);
}
