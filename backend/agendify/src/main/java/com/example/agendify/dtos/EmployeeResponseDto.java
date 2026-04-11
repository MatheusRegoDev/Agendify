package com.example.agendify.dtos;

import java.util.Set;
import java.util.UUID;

public record EmployeeResponseDto(
        UUID idEmployee,
        String name,
        String email,
        Set<String> roles
) {
}
