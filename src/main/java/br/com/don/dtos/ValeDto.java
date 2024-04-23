package br.com.don.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.PositiveOrZero;

public record ValeDto(

    @PositiveOrZero
    Long colaboradorId,

    @PositiveOrZero
    BigDecimal valor,

    LocalDate data

) {
}