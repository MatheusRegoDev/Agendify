package com.example.agendify.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClientRequestDto(

        @NotBlank String name,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "O telefone deve conter exatamente o DDD e número com o 9") String cellPhone) {
}
