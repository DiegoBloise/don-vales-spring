package br.com.don.controller;

import java.math.BigDecimal;
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

import br.com.don.dto.ColaboradorDto;
import br.com.don.dto.ValeDto;
import br.com.don.service.ColaboradorService;
import br.com.don.service.ValeService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/colaboradores")
@CrossOrigin(origins = "http://localhost:5173")
public class ColaboradorController {

    @Autowired
    private ColaboradorService service;

    @Autowired
    private ValeService valeService;


    @PostMapping
    public ResponseEntity<Object> saveColaborador(@RequestBody @Valid ColaboradorDto colaboradorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarColaborador(colaboradorDto));
    }


    @GetMapping
    public ResponseEntity<List<ColaboradorDto>> getAllColaboradores() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listar());
    }


    @GetMapping("/{id}/vales")
    public ResponseEntity<List<ValeDto>> getAllValesColaborador(@PathVariable(value="id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(valeService.buscarPorColaboradorId(id));
    }


    @GetMapping("/{id}/vales/total")
    public ResponseEntity<BigDecimal> getTotalValesColaborador(@PathVariable(value="id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(valeService.totalDoColaborador(id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneColaborador(@PathVariable(value="id") Long id) {
        ColaboradorDto responseColaboradorDto = service.getDtoById(id);

        if(responseColaboradorDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseColaboradorDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateColaborador(@PathVariable(value="id") Long id, @RequestBody @Valid ColaboradorDto colaboradorDto) {
        ColaboradorDto responseColaboradorDto = service.getDtoById(id);

        if(responseColaboradorDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador not found.");
        }

        colaboradorDto.setId(id);

        return ResponseEntity.status(HttpStatus.OK).body(service.salvarColaborador(colaboradorDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteColaborador(@PathVariable(value="id") Long id) {

        ColaboradorDto responseColaboradorDto = service.getDtoById(id);

        if(responseColaboradorDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Colaborador not found.");
        }

        service.deletarColaboradorPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body("Colaborador deleted successfully.");
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
