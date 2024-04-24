package br.com.don.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.don.enums.TipoChavePix;

@RestController
@RequestMapping("/api/tipos-chave-pix")
@CrossOrigin(origins = "http://localhost:5173")
public class TipoChavePixController {

    @GetMapping
    public ResponseEntity<List<Object>> getAllTiposChavePix() {
        List<Object> tipos = Arrays.asList(
            formatTipoChavePix(TipoChavePix.CELULAR),
            formatTipoChavePix(TipoChavePix.CPF),
            formatTipoChavePix(TipoChavePix.CNPJ),
            formatTipoChavePix(TipoChavePix.EMAIL),
            formatTipoChavePix(TipoChavePix.ALEATORIA)
        );
        return ResponseEntity.status(HttpStatus.OK).body(tipos);
    }

    private Object formatTipoChavePix(TipoChavePix tipoChavePix) {
        return new Object() {
            @SuppressWarnings("unused")
            public String tipo = tipoChavePix.name();
            @SuppressWarnings("unused")
            public String descricao = tipoChavePix.getDescricao();
        };
    }
}
