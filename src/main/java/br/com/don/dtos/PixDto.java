package br.com.don.dtos;

import br.com.don.enums.TipoChavePix;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record PixDto(

    @NotBlank
    TipoChavePix tipo,

    @NotEmpty
    String chave

) {
}