package com.example.agendify.services;

import com.example.agendify.dtos.ClientRequestDto;
import com.example.agendify.dtos.ClientResponeDto;
import com.example.agendify.models.ClientModel;
import com.example.agendify.repositories.ClientRepositoy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepositoy clientRepositoy;

    public ClientService(ClientRepositoy clientRepositoy) {
        this.clientRepositoy = clientRepositoy;
    }

    private ClientResponeDto toResponseDto(ClientModel client) {
        return new ClientResponeDto(
                client.getIdClient(),
                client.getName(),
                client.getCellPhone()
        );
    }

    @Transactional
    public ClientResponeDto saveClient(ClientRequestDto clientRequestDto) {
        if (clientRepositoy.existsByNameIgnoreCase(clientRequestDto.name())) {
            throw new IllegalArgumentException(
                    "Cliente: " + clientRequestDto.name() + " já foi cadastrado"
            );
        }

        var clientModel = new ClientModel();
        BeanUtils.copyProperties(clientRequestDto, clientModel);

        return toResponseDto(clientRepositoy.save(clientModel));
    }

      public List<ClientResponeDto> getAllClients() {
        return clientRepositoy.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }


    public ClientModel getOneClientByName(String name){
        return clientRepositoy.findByNameIgnoreCase(name)
                .orElseThrow(() ->new NoSuchElementException("Cliente não encontrado com o nome: " + name));
    }

    @Transactional
    public ClientModel updateClient(UUID id, ClientRequestDto clientRequestDto){
        ClientModel existingClient = clientRepositoy.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente com id: " + id +  "não encontrado"));

        if (clientRepositoy.existsByNameIgnoreCaseAndIdClientNot(clientRequestDto.name(), id)) {
            throw new IllegalArgumentException("Já existe um cliente com o nome: " + clientRequestDto.name());
        }

        BeanUtils.copyProperties(clientRequestDto, existingClient, "idClient");
        return clientRepositoy.save(existingClient);

    }

    @Transactional
    public void deleteClientById(UUID id){
        if (!clientRepositoy.existsById(id)){
            throw new NoSuchElementException("Cliente não encontrado");
        }
        clientRepositoy.deleteById(id);
    }
}
