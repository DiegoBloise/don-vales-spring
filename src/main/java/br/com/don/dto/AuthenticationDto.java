package br.com.don.dto;

import jakarta.validation.constraints.NotEmpty;


public record AuthenticationDto(
    @NotEmpty
    String username,

    @NotEmpty
    String password
) {
}