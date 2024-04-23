package br.com.don.dtos;

import java.time.LocalDate;

import br.com.don.enums.TipoColaborador;
import jakarta.validation.constraints.NotEmpty;

public record FreelancerDto(

    Long id,

    @NotEmpty
    String nome,

    @NotEmpty
    String telefone,

    LocalDate dataNascimento,

    TipoColaborador tipo,

    PixDto pix

) {

}