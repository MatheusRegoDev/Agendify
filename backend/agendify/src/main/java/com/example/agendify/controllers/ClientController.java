package com.example.agendify.controllers;

import com.example.agendify.dtos.ClientRecordDto;
import com.example.agendify.models.ClientModel;
import com.example.agendify.repositories.ClientRepositoy;
import com.example.agendify.services.ClientService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Object> saveClient(@RequestBody @Valid ClientRecordDto clientRecordDto) {
        try {
            ClientModel savedClient = clientService.saveClient(clientRecordDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<ClientModel>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getOneClientByName(@RequestParam String name){
        try {
            return ResponseEntity.ok(clientService.getOneClientByName(name));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateClient(@PathVariable UUID id, @RequestBody @Valid ClientRecordDto clientRecordDto){
        try {
            return ResponseEntity.ok(clientService.updateClient(id, clientRecordDto));
        } catch (NoSuchElementException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteClientByName(@RequestParam String name) {
        try {
            clientService.deleteClientByName(name);
            return ResponseEntity.ok("Cliente " + name + " deletado com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}
