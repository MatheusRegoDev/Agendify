package com.example.agendify.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClientRecordDto(@NotBlank String name,
                              @NotBlank @Pattern(regexp = "\\d{11}", message = "O telefone deve conter exatamente 9 dígitos")
                              String cellPhone) {
}
