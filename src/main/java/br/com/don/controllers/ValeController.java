package br.com.don.controllers;

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

import br.com.don.dtos.ValeDto;
import br.com.don.services.ValeService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/vales")
@CrossOrigin(origins = "http://localhost:5173")
public class ValeController {

    @Autowired
    private ValeService service;


    @PostMapping
    public ResponseEntity<ValeDto> saveVale(@RequestBody @Valid ValeDto colaboradorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarVale(colaboradorDto));
    }


    @GetMapping
    public ResponseEntity<List<ValeDto>> getAllVales() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listar());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneVale(@PathVariable(value="id") Long id) {
        ValeDto responseValeDto = service.buscarPorId(id);

        if(responseValeDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vale not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseValeDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVale(@PathVariable(value="id") Long id, @RequestBody @Valid ValeDto colaboradorDto) {
        ValeDto responseValeDto = service.buscarPorId(id);

        if(responseValeDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vale not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(service.salvarVale(responseValeDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVale(@PathVariable(value="id") Long id) {

        ValeDto responseValeDto = service.buscarPorId(id);

        if(responseValeDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vale not found.");
        }

        service.deletarValePorId(id);

        return ResponseEntity.status(HttpStatus.OK).body("Vale deleted successfully.");
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
