package com.example.agendify.controllers;

import com.example.agendify.dtos.ClientRequestDto;
import com.example.agendify.dtos.ClientResponeDto;
import com.example.agendify.models.ClientModel;
import com.example.agendify.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {


    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_RECEPTIONIST')")
    public ResponseEntity<Object> saveClient(@RequestBody @Valid ClientRequestDto clientRequestDto) {
        try {
            ClientResponeDto savedClient = clientService.saveClient(clientRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_RECEPTIONIST')")
    public ResponseEntity<List<ClientResponeDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_RECEPTIONIST')")
    public ResponseEntity<Object> getOneClientByName(@RequestParam String name){
        try {
            return ResponseEntity.ok(clientService.getOneClientByName(name));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_RECEPTIONIST')")
    public ResponseEntity<Object> updateClient(@PathVariable UUID id, @RequestBody @Valid ClientRequestDto clientRequestDto){
        try {
            return ResponseEntity.ok(clientService.updateClient(id, clientRequestDto));
        } catch (NoSuchElementException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_RECEPTIONIST')")
    public ResponseEntity<Object> deleteClientById(@RequestParam UUID id) {
        try {
            clientService.deleteClientById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}
