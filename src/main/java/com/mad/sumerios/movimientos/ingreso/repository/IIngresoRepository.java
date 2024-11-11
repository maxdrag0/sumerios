package com.mad.sumerios.movimientos.ingreso.repository;

import com.mad.sumerios.movimientos.ingreso.model.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Repository
public interface IIngresoRepository extends JpaRepository<Ingreso, Long> {
    List<Ingreso> findByIdConsorcioAndFechaBetween(Long idConsorcio, Date startDate, Date endDate);
    List<Ingreso> findByIdConsorcio (Long idConsorcio);
    List<Ingreso> findByIdProveedor (Long idProveedor);
    List<Ingreso> findByIdProveedorAndIdConsorcio (Long idProveedor, Long idConsorcio);
    List<Ingreso> findByExpensa_IdExpensa (Long idExpensa);
}
