package com.example.agendify.services;

import com.example.agendify.dtos.ClientRecordDto;
import com.example.agendify.models.ClientModel;
import com.example.agendify.repositories.ClientRepositoy;
import jakarta.persistence.Id;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.BeanEntry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    ClientRepositoy clientRepositoy;

    public ClientModel saveClient(ClientRecordDto clientRecordDto) {
        if(clientRepositoy.existsByNameIgnoreCase(clientRecordDto.name())){
            throw new IllegalArgumentException("Cliente: " + clientRecordDto.name() +  "já foi cadastrado");
        }

        var clientModel = new ClientModel();
        BeanUtils.copyProperties(clientRecordDto, clientModel);

        return clientRepositoy.save(clientModel);
    }

    public List<ClientModel> getAllClients() {
        return clientRepositoy.findAll();
    }

    public ClientModel getOneClientByName(String name){
        return clientRepositoy.findByNameIgnoreCase(name)
                .orElseThrow(() ->new NoSuchElementException("Cliente não encontrado com o nome: " + name));
    }

    public ClientModel updateClient(UUID id, ClientRecordDto clientRecordDto){
        ClientModel existingClient = clientRepositoy.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente com id: " + id +  "não encontrado"));

        if (clientRepositoy.existsByNameIgnoreCaseAndIdClientNot(clientRecordDto.name(), id)) {
            throw new IllegalArgumentException("Já existe um cliente com o nome: " + clientRecordDto.name());
        }

        BeanUtils.copyProperties(clientRecordDto, existingClient, "idClient");
        return clientRepositoy.save(existingClient);

    }

    @Transactional
    public void deleteClientByName(String name){
        if (!clientRepositoy.existsByNameIgnoreCase(name)){
            throw new NoSuchElementException("Cliente não encontrado");
        }
        clientRepositoy.deleteByNameIgnoreCase(name);
    }
}
