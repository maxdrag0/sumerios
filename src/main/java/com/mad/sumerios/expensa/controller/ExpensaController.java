package com.mad.sumerios.expensa.controller;

import com.mad.sumerios.consorcio.model.Consorcio;
import com.mad.sumerios.consorcio.repository.IConsorcioRepository;
import com.mad.sumerios.expensa.dto.ExpensaCreateDTO;
import com.mad.sumerios.expensa.dto.ExpensaResponseDto;
import com.mad.sumerios.expensa.dto.LiquidarExpensaRequest;
import com.mad.sumerios.expensa.service.ExpensaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    // LISTAR TODAS
    @GetMapping
    public ResponseEntity<?> getAllExpensas(){
        try{
            List<ExpensaResponseDto> expensas = expensaService.getExpensas();
            if(expensas.isEmpty()){
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No se encontraron expensas.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            return ResponseEntity.ok(expensas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // POR ID
    @GetMapping("/{idExpensa}")
    public ResponseEntity<?> getExpensaById(@PathVariable Long idExpensa){
        try{
            ExpensaResponseDto expensa = expensaService.getExpensasById(idExpensa);
            return ResponseEntity.ok(expensa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // LISTAR CONSORCIO
    @GetMapping("/consorcio/{idConsorcio}")
    public ResponseEntity<?> getExpensasByConsorcio(@PathVariable Long idConsorcio){
        try{
            List<ExpensaResponseDto> expensas = expensaService.getExpensasByConsorcio(idConsorcio);
            if(expensas.isEmpty()){
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No se encontraron expensas.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            return ResponseEntity.ok(expensas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    // CONSORCIO Y PERIODO
    @GetMapping("/consorcios/{idConsorcio}/periodo/{periodo}")
    public ResponseEntity<?> getExpensasByConsorcioAndPeriodo(@PathVariable Long idConsorcio, @PathVariable YearMonth periodo){
        try{
            ExpensaResponseDto expensa = expensaService.getExpensasByConsorcioAndPeriodo(idConsorcio, periodo);
            return ResponseEntity.ok(expensa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/consorcio/{idConsorcio}")
    public ResponseEntity<?> liquidarExpensa (@PathVariable Long idConsorcio,
                                              @RequestBody LiquidarExpensaRequest request){
        try{
            expensaService.liquidarExpensaMesVencido(idConsorcio, request.getIdExpensa(), request.getExpensaCreateDTO(), request.getRepetirEgresos());
            return ResponseEntity.status(HttpStatus.CREATED).body("Expensa liquidada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idExpensa}")
    public ResponseEntity<?> restablecerPeriodo(@PathVariable Long idExpensa){
        try{
            ExpensaResponseDto dto = expensaService.restablecerPeriodo(idExpensa);
            return ResponseEntity.status(HttpStatus.OK).body("Periodo "+dto.getPeriodo()+" restablecido con éxito.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
