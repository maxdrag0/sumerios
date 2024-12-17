package com.mad.sumerios.intermedioExpensaConsorcio.controller;

import com.mad.sumerios.intermedioExpensaConsorcio.dto.IntermediaExpensaConsorcioCreateDto;
import com.mad.sumerios.intermedioExpensaConsorcio.dto.IntermediaExpensaConsorcioDto;
import com.mad.sumerios.intermedioExpensaConsorcio.service.IntermediaExpensaConsorcioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intermedia_expensa_consorcio")
public class IntermediaExpensaConsorcioController {

    private final IntermediaExpensaConsorcioService intermediaExpensaConsorcioService;

    @Autowired
    public IntermediaExpensaConsorcioController (IntermediaExpensaConsorcioService intermediaExpensaConsorcioService){
        this.intermediaExpensaConsorcioService = intermediaExpensaConsorcioService;
    }

    @PostMapping
    public ResponseEntity<String> createIntermediaExpensaConsorcio (@RequestBody IntermediaExpensaConsorcioCreateDto dto){
        try {
            intermediaExpensaConsorcioService.createIntermediaExpensaConsorcio(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Intermedia creada exitosamente");

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping ("/{idIntermedia}")
    public ResponseEntity<String> updateIntermedia(@PathVariable Long idIntermedia,
                                                   @RequestBody IntermediaExpensaConsorcioDto dto){
        try{
            intermediaExpensaConsorcioService.updateIntermediaExpensaConsorcio(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Intermedia modificada exitosamente");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/consorcio/{idConsorcio}")
    public ResponseEntity<?> getIntermediaByConsorcio(@PathVariable Long idConsorcio){
        try{
            IntermediaExpensaConsorcioDto dto = intermediaExpensaConsorcioService.getIntermediaByConsorcio(idConsorcio);
            return ResponseEntity.ok(dto);
        }   catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idIntermedia}")
    public ResponseEntity<String> deleteIntermedia (@PathVariable Long idIntermedia) {
        try{
            intermediaExpensaConsorcioService.deleteIntermedia(idIntermedia);
            return ResponseEntity.status(HttpStatus.OK).body(
                    "Intermedia eliminada exitosamente"
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
