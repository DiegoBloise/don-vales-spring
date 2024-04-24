package br.com.don.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.PositiveOrZero;

public record ValeDto(

    Long id,

    @PositiveOrZero
    Long colaboradorId,

    @PositiveOrZero
    Float valor,

    LocalDate data

) {
}