package com.example.agendify.controllers;

import com.example.agendify.dtos.EmployeeDto;
import com.example.agendify.dtos.EmployeeResponseDto;
import com.example.agendify.dtos.EmployeeUpdateDto;
import com.example.agendify.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeeService employeeService;

    public EmployeesController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(employeeDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<EmployeeResponseDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Object> getOneEmployeeByEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(employeeService.getOneEmployeeByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

     @PutMapping("/{id}")
     @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
     public ResponseEntity<Object> updateEmployee(@PathVariable UUID id, @RequestBody @Valid EmployeeUpdateDto employeeUpdateDto) {
         try {
             return ResponseEntity.ok(employeeService.updateEmployee(id, employeeUpdateDto));
         } catch (NoSuchElementException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
         } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
         }
     }

     @DeleteMapping("/{id}")
     @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
     public ResponseEntity<Object> deleteEmployeeById(@PathVariable UUID id) {
         try {
             employeeService.deleteEmployeeById(id);
             return ResponseEntity.noContent().build();
         } catch (NoSuchElementException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
         } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
         }
     }
}
