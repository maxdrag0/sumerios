package com.mad.sumerios.movimientos.egreso.repository;

import com.mad.sumerios.movimientos.egreso.model.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IEgresoRepository extends JpaRepository<Egreso, Long> {
    List<Egreso> findByIdConsorcio(Long idConsorcio);
    List<Egreso> findByIdConsorcioAndFechaBetween(Long idConsorcio, Date startDate, Date endDate);
    List<Egreso> findByIdProveedor(Long idProveedor);
    List<Egreso> findByIdProveedorAndIdConsorcio(Long idProveedor, Long idConsorcio);
    List<Egreso> findByTotalFinal(Double totalFinal);
    Egreso findByComprobante(String comprobante);
    //    List<Egreso> findByExpensa_IdExpensa(Long idExpensa);
}
