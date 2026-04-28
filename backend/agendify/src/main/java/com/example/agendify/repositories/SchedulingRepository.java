package com.example.agendify.repositories;

import com.example.agendify.models.EmployeeModel;
import com.example.agendify.models.SchedulingModel;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SchedulingRepository extends JpaRepository<SchedulingModel, UUID> {
    List<SchedulingModel> findBySchedulingDate(LocalDate schedulingDate);

    List<SchedulingModel> findByClient_IdClient(UUID idClient);

    List<SchedulingModel> findByStatus(SchedulingModel.Status status);

    boolean existsByClient_IdClientAndSchedulingDateAndStatusNot(
            UUID idClient, LocalDate schedulingDate, SchedulingModel.Status status
    );

    long countBySchedulingDateAndStatusNot(LocalDate schedulingDate, SchedulingModel.Status status);
}
