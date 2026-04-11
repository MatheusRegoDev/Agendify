package com.example.agendify.dtos;

import java.util.UUID;

public record ClientResponeDto(
        UUID idClient,
        String name,
        String cellPhone
) {
}
