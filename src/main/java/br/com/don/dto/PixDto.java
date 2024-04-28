package br.com.don.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.don.enums.TipoChavePix;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class PixDto {

    Long id;

    @NotNull
    TipoChavePix tipo;

    @NotEmpty
    String chave;
}