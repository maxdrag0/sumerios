package com.mad.sumerios.expensa.repository;

import com.mad.sumerios.expensa.model.Expensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface IExpensaRepository extends JpaRepository<Expensa, Long> {
    Expensa findByidConsorcioAndPeriodo(Long idConsorcio, YearMonth periodo);
    List<Expensa> findByidConsorcio(Long idConsorcio);
}
