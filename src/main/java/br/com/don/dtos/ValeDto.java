package br.com.don.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.PositiveOrZero;

public record ValeDto(

    @PositiveOrZero
    BigDecimal valor,

    LocalDate data

) {
}