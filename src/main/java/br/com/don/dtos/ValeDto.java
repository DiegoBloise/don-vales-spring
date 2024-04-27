package br.com.don.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.don.enums.TipoVale;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@JsonInclude(Include.NON_EMPTY)
public class ValeDto {

    Long colaboradorId;

    String colaboradorNome;

    Long id;

    @PositiveOrZero
    BigDecimal valor;

    TipoVale tipo;

    LocalDate data;
}