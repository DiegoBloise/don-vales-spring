package br.com.don.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.don.enums.TipoVale;

@RestController
@RequestMapping("/api/tipos-vales")
@CrossOrigin(origins = "http://localhost:5173")
public class TipoValeController {

    @GetMapping
    public ResponseEntity<List<Object>> getAllTiposVales() {
        List<Object> tipos = Arrays.asList(
            formatTipoVale(TipoVale.DINHEIRO),
            formatTipoVale(TipoVale.SALDO)
        );
        return ResponseEntity.status(HttpStatus.OK).body(tipos);
    }

    private Object formatTipoVale(TipoVale tipoVale) {
        return new Object() {
            @SuppressWarnings("unused")
            public String tipo = tipoVale.name();
            @SuppressWarnings("unused")
            public String descricao = tipoVale.getDescricao();
        };
    }
}
