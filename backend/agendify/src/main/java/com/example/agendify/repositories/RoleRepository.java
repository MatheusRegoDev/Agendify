package com.example.agendify.repositories;

import com.example.agendify.models.EmployeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<EmployeeModel, Long> {
}
