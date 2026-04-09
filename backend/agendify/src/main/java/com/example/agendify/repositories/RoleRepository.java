package com.example.agendify.repositories;

import com.example.agendify.models.EmployeeModel;
import com.example.agendify.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    @Override
    Optional<RoleModel> findById(Long idRole);
    Optional<RoleModel> findByName(String name);
}
