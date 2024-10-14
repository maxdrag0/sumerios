package com.mad.sumerios.pendientes.movimientos.egreso.repository;

import com.mad.sumerios.pendientes.movimientos.egreso.model.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IEgresoRepository extends JpaRepository<Egreso, Long> {
    List<Egreso> findByConsorcio_IdConsorcioAndFechaBetween(Long idConsorcio, Date fechaInicio, Date fechaFin);
    List<Egreso> findByIdProveedor(Long idProveedor);
}
