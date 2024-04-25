package br.com.don.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class ValeDto {

    Long id;

    @PositiveOrZero
    Float valor;

    LocalDate data;
}