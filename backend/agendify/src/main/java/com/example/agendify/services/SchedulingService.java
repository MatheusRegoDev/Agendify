package com.example.agendify.services;

import com.example.agendify.dtos.ReschedulingRequestDto;
import com.example.agendify.dtos.SchedulingRequestDto;
import com.example.agendify.dtos.SchedulingResponseDto;
import com.example.agendify.models.SchedulingModel;
import com.example.agendify.repositories.ClientRepositoy;
import com.example.agendify.repositories.SchedulingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class SchedulingService {
    @Value("${scheduling.dailyLimit:20}")
    private int dailyLimit;

    private final SchedulingRepository schedulingRepository;
    private final ClientRepositoy clientRepositoy;

    public SchedulingService(SchedulingRepository schedulingRepository, ClientRepositoy clientRepositoy) {
        this.schedulingRepository = schedulingRepository;
        this.clientRepositoy = clientRepositoy;
    }

    private SchedulingResponseDto toResponseDto(SchedulingModel scheduling) {
        return new SchedulingResponseDto(
                scheduling.getIdScheduling(),
                scheduling.getClient().getIdClient(),
                scheduling.getClient().getName(),
                scheduling.getSchedulingDate(),
                scheduling.getStatus()
        );
    }

    private SchedulingModel findAndValidateStatus(UUID idClient, SchedulingModel.Status requiredStatus) {
        var scheduling = schedulingRepository.findById(idClient)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado para o cliente: " + idClient));

        if (scheduling.getStatus() != requiredStatus) {
            throw new IllegalStateException("Agendamento não está no status requerido: " + requiredStatus);
        }

        return scheduling;
    }

    @Transactional
    public SchedulingResponseDto createScheduling(SchedulingRequestDto schedulingRequestDto){
        var client = schedulingRepository.findById(schedulingRequestDto.idClient())
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));

        long countSchedulings = schedulingRepository.countBySchedulingDateAndStatusNot(
                schedulingRequestDto.shedulingDate(),
                SchedulingModel.Status.CANCELED
        );



        if (countSchedulings >= dailyLimit) {
            throw new IllegalArgumentException("Limite de agendamentos para esta data atingido");
        }

        boolean alreadyScheduling = schedulingRepository
                .existsByClient_IdClientAndSchedulingDateAndStatusNot(
                        schedulingRequestDto.idClient(),
                        schedulingRequestDto.shedulingDate(),
                        SchedulingModel.Status.CANCELED
                );

        if (alreadyScheduling) {
            throw new IllegalStateException("Cliente já possui um agendamento para esta data");
        }

        var scheduling = new SchedulingModel();
        scheduling.setClient(client.getClient());
        scheduling.setSchedulingDate(schedulingRequestDto.shedulingDate());
        scheduling.setStatus(SchedulingModel.Status.SCHEDULED);

        return toResponseDto(schedulingRepository.save(scheduling));
    }

    public List<SchedulingResponseDto> getAllSchedulings() {
        return schedulingRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public SchedulingResponseDto getSchedulingByIdClient(UUID idClient) {
        var scheduling = schedulingRepository.findByClient_IdClient(idClient)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado para o cliente: " + idClient));

        return toResponseDto(scheduling);
    }

    @Transactional
    public SchedulingResponseDto comfirmPresence(UUID idClient){
        var scheduling = findAndValidateStatus(idClient, SchedulingModel.Status.SCHEDULED);

        scheduling.setStatus(SchedulingModel.Status.CONFIRMED);
        return toResponseDto(schedulingRepository.save(scheduling));
    }

    @Transactional
    public SchedulingResponseDto cancelScheduling(UUID idClient){
        var scheduling = schedulingRepository.findById(idClient)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado para o cliente: " + idClient));

        if (scheduling.getStatus() == SchedulingModel.Status.CANCELED) {
            throw new IllegalStateException("Agendamento já está cancelado para o cliente: " + idClient);
        }

        scheduling.setStatus(SchedulingModel.Status.CANCELED);
        return toResponseDto(schedulingRepository.save(scheduling));
    }

     @Transactional
    public SchedulingResponseDto rescheduleScheduling(UUID idClient, ReschedulingRequestDto dto){
        var oldScheduling = schedulingRepository.findById(idClient)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado para o cliente: " + idClient));

        if (oldScheduling.getStatus() == SchedulingModel.Status.CANCELED) {
            throw new IllegalStateException("Agendamento está cancelado para o cliente: " + idClient);
        }

        oldScheduling.setStatus(SchedulingModel.Status.RESCHEDULED);
        schedulingRepository.save(oldScheduling);

        var newScheduling = new SchedulingModel();
        newScheduling.setClient(oldScheduling.getClient());
        newScheduling.setSchedulingDate(dto.newSchedulingDate());
        newScheduling.setStatus(SchedulingModel.Status.SCHEDULED);

        return toResponseDto(schedulingRepository.save(newScheduling));
     }

}
