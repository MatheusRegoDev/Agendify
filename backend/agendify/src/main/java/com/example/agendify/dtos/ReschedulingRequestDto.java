package com.example.agendify.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReschedulingRequestDto(
        @NotNull @Future(message = "A data de agendamento deve ser futura") LocalDate newSchedulingDate
        ) {
}
