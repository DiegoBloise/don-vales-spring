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

import br.com.don.dtos.FreelancerDto;
import br.com.don.services.FreelancerService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/freelancers")
@CrossOrigin(origins = "http://localhost:5173")
public class FreelancerController {

    @Autowired
    private FreelancerService service;


    @PostMapping
    public ResponseEntity<Object> saveFreelancer(@RequestBody @Valid FreelancerDto freelancerDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarFreelancer(freelancerDto));
    }


    @GetMapping
    public ResponseEntity<List<FreelancerDto>> getAllFreelancers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listar());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneFreelancer(@PathVariable(value="id") Long id) {
        FreelancerDto responseFreelancerDto = service.getDtoById(id);

        if(responseFreelancerDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Freelancer not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseFreelancerDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFreelancer(@PathVariable(value="id") Long id, @RequestBody @Valid FreelancerDto freelancerDto) {
        FreelancerDto responseFreelancerDto = service.getDtoById(id);

        if(responseFreelancerDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Freelancer not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(service.salvarFreelancer(freelancerDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFreelancer(@PathVariable(value="id") Long id) {

        FreelancerDto responseFreelancerDto = service.getDtoById(id);

        if(responseFreelancerDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Freelancer not found.");
        }

        service.deletarFreelancerPorId(id);

        return ResponseEntity.status(HttpStatus.OK).body("Freelancer deleted successfully.");
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
