package com.example.agendify.services;

import com.example.agendify.dtos.LoginRequestDto;
import com.example.agendify.dtos.LoginResponseDto;
import com.example.agendify.models.RoleModel;
import com.example.agendify.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final JwtEncoder jwtEncoder;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.expiration}")
    private Long expiration;

    public AuthService(EmployeeRepository employeeRepository,
                       JwtEncoder jwtEncoder,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.jwtEncoder = jwtEncoder;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public LoginResponseDto Login (LoginRequestDto loginRequestDto) {
        var employee = employeeRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com o email: " + loginRequestDto.email()));

        if (!bCryptPasswordEncoder.matches(loginRequestDto.password(), employee.getPassword())) {
            throw new IllegalArgumentException("Senha incorreta para o email: " + loginRequestDto.email());
        }

        var now = Instant.now();
        var expirensIn = 300L;

        String roles = employee.getRoles().stream()
                .map(RoleModel::getName)
                .collect(Collectors.joining(" "));

        var scopes = employee.getRoles().stream()
                .map(RoleModel::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("Agendify")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirensIn))
                .subject(employee.getEmail())
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponseDto(jwtValue, expirensIn);
    }
}
