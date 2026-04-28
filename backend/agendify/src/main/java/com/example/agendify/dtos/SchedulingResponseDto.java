package com.example.agendify.dtos;

import com.example.agendify.models.SchedulingModel;

import java.time.LocalDate;
import java.util.UUID;

public record SchedulingResponseDto(
        UUID idScheduling,
        UUID idClient,
        String clientName,
        LocalDate schedulingDate,
        SchedulingModel.Status status
) {
}
