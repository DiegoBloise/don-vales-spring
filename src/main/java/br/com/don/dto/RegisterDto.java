package br.com.don.dto;

import br.com.don.enums.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegisterDto(
    @NotEmpty
    String username,

    @NotEmpty
    String password,

    @NotNull
    UserRole role
) {

}