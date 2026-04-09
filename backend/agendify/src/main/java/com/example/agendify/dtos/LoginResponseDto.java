package com.example.agendify.dtos;

public record LoginResponseDto(
        String accessToken,
        Long expiresIn
) {
}
