package br.com.don.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.don.enums.TipoChavePix;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class PixDto {

    Long id;

    @NotBlank
    TipoChavePix tipo;

    @NotEmpty
    String chave;
}