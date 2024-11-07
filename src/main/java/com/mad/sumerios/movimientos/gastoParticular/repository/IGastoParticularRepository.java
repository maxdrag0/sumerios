package com.mad.sumerios.movimientos.gastoParticular.repository;

import com.mad.sumerios.movimientos.gastoParticular.model.GastoParticular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IGastoParticularRepository extends JpaRepository<GastoParticular, Long> {
    List<GastoParticular> findByIdProveedor(Long idProveedor);
    List<GastoParticular> findByIdUf(Long idUf);
    List<GastoParticular> findByIdUfAndFechaBetween(Long idConsorcio, Date startDate, Date endDate);
    List<GastoParticular> findByIdConsorcio(Long idConsorcio);
    List<GastoParticular> findByIdConsorcioAndFechaBetween(Long idConsorcio, Date startDate, Date endDate);
    List<GastoParticular> findByTotalFinal(Double totalFinal);
    GastoParticular findByComprobante(String comprobante);
}
