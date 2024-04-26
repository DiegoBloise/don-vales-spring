package br.com.don.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.don.enums.TipoColaborador;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class ColaboradorDto {

    Long id;

    @NotEmpty
    String nome;

    @NotEmpty
    String telefone;

    @NotNull
    LocalDate dataNascimento;

    @NotNull
    TipoColaborador tipo;

    @NotNull
    PixDto pix;
}