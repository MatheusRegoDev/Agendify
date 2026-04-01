package com.example.agendify.repositories;

import com.example.agendify.models.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepositoy  extends JpaRepository<ClientModel, UUID> {

    List<ClientModel> findByNameContainingIgnoreCaseAndCellPhone(String name, String cellPhone);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdClientNot(String name, UUID idClient);

    Optional<ClientModel> findByNameIgnoreCase(String name);

    void deleteByNameIgnoreCase(String name);
}
