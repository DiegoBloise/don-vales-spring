package br.com.don.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.don.enums.TipoColaborador;

@RestController
@RequestMapping("/api/tipos-colaboradores")
@CrossOrigin(origins = "http://localhost:5173")
public class TipoColaboradorController {

    @GetMapping
    public ResponseEntity<List<Object>> getAllTiposColaboradores() {
        List<Object> tipos = Arrays.asList(
            formatTipoColaborador(TipoColaborador.FIXO),
            formatTipoColaborador(TipoColaborador.ENTREGADOR),
            formatTipoColaborador(TipoColaborador.FREELANCER)
        );
        return ResponseEntity.status(HttpStatus.OK).body(tipos);
    }

    private Object formatTipoColaborador(TipoColaborador tipoColaborador) {
        return new Object() {
            @SuppressWarnings("unused")
            public String tipo = tipoColaborador.name();
            @SuppressWarnings("unused")
            public String descricao = tipoColaborador.getDescricao();
        };
    }
}
