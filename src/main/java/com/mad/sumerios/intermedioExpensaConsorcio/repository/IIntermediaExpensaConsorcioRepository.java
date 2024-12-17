package com.mad.sumerios.intermedioExpensaConsorcio.repository;

import com.mad.sumerios.intermedioExpensaConsorcio.model.IntermediaExpensaConsorcio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIntermediaExpensaConsorcioRepository extends JpaRepository<IntermediaExpensaConsorcio, Long> {
    IntermediaExpensaConsorcio findByIdConsorcio (Long idConsorcio);
}
