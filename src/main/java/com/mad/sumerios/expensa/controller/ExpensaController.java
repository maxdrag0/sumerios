package com.mad.sumerios.expensa.controller;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.expensa.dto.ExpensaCreateDTO;
import com.mad.sumerios.expensa.service.ExpensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/expensas")
public class ExpensaController {

    private final ExpensaService expensaService;
    private final IConsorcioRepository consorcioRepository;

    @Autowired
    public ExpensaController(ExpensaService expensaService,
                             IConsorcioRepository consorcioRepository){
        this.expensaService = expensaService;
        this.consorcioRepository = consorcioRepository;
    }

    @PostMapping
    public ResponseEntity<String> createExpensa(@RequestBody ExpensaCreateDTO dto) {
        try{
            expensaService.createExpensa(dto);
            Consorcio consorcio = consorcioRepository.findById(dto.getIdConsorcio()).get();
            return ResponseEntity.status(HttpStatus.CREATED).body("Expensa período: "+ dto.getPeriodo()+
                                                                  " del consorcio "+consorcio.getNombre()+
                                                                  " creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
