package com.example.agendify.dtos;

import jakarta.validation.constraints.*;

import java.util.Set;

public record EmployeeUpdateDto (
        @NotBlank String name,
        @NotBlank @Email String email,
        @Size(min=8, message = "A senha deve ter no mínimo 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
        String password,
        @NotEmpty Set<String> roles
) {
}
