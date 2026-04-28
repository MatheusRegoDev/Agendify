package com.example.agendify.controllers;

import com.example.agendify.dtos.ReschedulingRequestDto;
import com.example.agendify.dtos.SchedulingRequestDto;
import com.example.agendify.dtos.SchedulingResponseDto;
import com.example.agendify.services.SchedulingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/schedulings")
public class SchedulinngController {

    private final SchedulingService schedulingService;


    public SchedulinngController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<Object> createScheduling(@RequestBody @Valid SchedulingRequestDto schedulingRequestDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(schedulingService.createScheduling(schedulingRequestDto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_RECEPTIONIST')")
    public ResponseEntity<List<SchedulingResponseDto>> getAllSchedulings() {
        return ResponseEntity.ok(schedulingService.getAllSchedulings());
    }

    @GetMapping("/client/{idClient}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECEPTIONIST)")
    public ResponseEntity<Object> getSchedulingsByClientId(@PathVariable UUID idClient) {
        try {
            return ResponseEntity.ok(schedulingService.getSchedulingByIdClient(idClient));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{idScheduling}/comfirm")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<Object> confirmScheduling(@PathVariable UUID idScheduling) {
        try {
            return ResponseEntity.ok(schedulingService.comfirmPresence(idScheduling));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/{idScheduling}/cancel")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<Object> cancelScheduling(@PathVariable UUID idScheduling) {
        try {
            return ResponseEntity.ok(schedulingService.cancelScheduling(idScheduling));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/{idScheduling}/reschedule")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<Object> rescheduleScheduling(@PathVariable UUID idScheduling, @RequestBody @Valid ReschedulingRequestDto reschedulingRequestDto) {
        try {
            return ResponseEntity.ok(schedulingService.rescheduleScheduling(idScheduling, reschedulingRequestDto));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}