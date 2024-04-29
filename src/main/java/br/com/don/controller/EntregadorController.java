package br.com.don.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.don.dto.EntregadorDto;
import br.com.don.service.EntregadorService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/entregadores")
@CrossOrigin(origins = "http://localhost:5173")
public class EntregadorController {

    @Autowired
    private EntregadorService service;


    @PostMapping
    public ResponseEntity<Object> saveEntregador(@RequestBody @Valid EntregadorDto entregadorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarEntregador(entregadorDto));
    }


    @GetMapping
    public ResponseEntity<List<EntregadorDto>> getAllEntregadores() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listar());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneEntregador(@PathVariable(value="id") Long id) {
        EntregadorDto responseEntregadorDto = service.getDtoById(id);

        if(responseEntregadorDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entregador not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseEntregadorDto);
    }


    @GetMapping("/{inicio}/{fim}")
    public ResponseEntity<List<EntregadorDto>> getEntregadoresPorData(@PathVariable(value="inicio") String inicio, @PathVariable(value="inicio") String fim) {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarEntregadoresDtoPorDataInicioFim(inicio, fim));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEntregador(@PathVariable(value="id") Long id, @RequestBody @Valid EntregadorDto entregadorDto) {
        EntregadorDto responseEntregadorDto = service.getDtoById(id);

        if(responseEntregadorDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entregador not found.");
        }

        entregadorDto.setId(id);

        return ResponseEntity.status(HttpStatus.OK).body(service.salvarEntregador(entregadorDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEntregador(@PathVariable(value="id") Long id) {

        EntregadorDto responseEntregadorDto = service.getDtoById(id);

        if(responseEntregadorDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entregador not found.");
        }

        service.deletarEntregadorPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body("Entregador deleted successfully.");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exp
    ) {
        var errors = new HashMap<String, String>();
        exp.getBindingResult().getAllErrors()
            .forEach(error -> {
                var fieldName = ((FieldError) error).getField();
                var errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
