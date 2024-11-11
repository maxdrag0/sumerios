package com.mad.sumerios.expensa.repository;

import com.mad.sumerios.expensa.model.Expensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;

@Repository
public interface IExpensaRepository extends JpaRepository<Expensa, Long> {
    Expensa findByConsorcio_idConsorcioAndPeriodo(Long idConsorcio, YearMonth periodo);
}
