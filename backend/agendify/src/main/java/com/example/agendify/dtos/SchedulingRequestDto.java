package com.example.agendify.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record SchedulingRequestDto(
        @NotNull UUID idClient,
        @NotNull @Future(message = "A data de agendamento deve ser futura") LocalDate shedulingDate
        ) {
}
