package com.example.agendify.services;

import com.example.agendify.dtos.EmployeeDto;
import com.example.agendify.dtos.EmployeeResponseDto;
import com.example.agendify.dtos.EmployeeUpdateDto;
import com.example.agendify.models.EmployeeModel;
import com.example.agendify.models.RoleModel;
import com.example.agendify.repositories.EmployeeRepository;
import com.example.agendify.repositories.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private EmployeeResponseDto toResponseDto(EmployeeModel employeeModel){
        Set<String> roleNames = employeeModel.getRoles().stream()
                .map(RoleModel::getName)
                .collect(java.util.stream.Collectors.toSet());

        return new EmployeeResponseDto(
                employeeModel.getIdEmployee(),
                employeeModel.getName(),
                employeeModel.getEmail(),
                roleNames
        );
    }

    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeDto employeeDto){

        if(employeeRepository.existsByEmail(employeeDto.email())){
            throw new IllegalArgumentException("Email " + employeeDto.email() + " já cadastrado");
        }

        Set<RoleModel> roleNames = employeeDto.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Role " + roleName + " não encontrada")))
                        .collect(java.util.stream.Collectors.toSet());

        var employee = new EmployeeModel();
        employee.setName(employeeDto.name());
        employee.setEmail(employeeDto.email());
        employee.setPassword(bCryptPasswordEncoder.encode(employeeDto.password()));
        employee.setRoles(roleNames);

        var savedEmployee = employeeRepository.save(employee);

        return toResponseDto(savedEmployee);

    }

    public List<EmployeeResponseDto > getAllEmployees(){
        return employeeRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }



    public EmployeeResponseDto getOneEmployeeByEmail(String email){
        var employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário com email " + email + " não encontrado"));

        return toResponseDto(employee);
    }

    @Transactional
    public EmployeeResponseDto updateEmployee(UUID id, EmployeeUpdateDto employeeDto){
        EmployeeModel existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Funcionário com id " + id + " não encontrado"));

        if(employeeRepository.existsByEmail(employeeDto.email()) && !existingEmployee.getEmail().equals(employeeDto.email())){
            throw new IllegalArgumentException("Email " + employeeDto.email() + " já cadastrado");
        }

        Set<RoleModel> roleNames = employeeDto.roles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Role " + roleName + " não encontrada")))
                .collect(java.util.stream.Collectors.toSet());

        existingEmployee.setName(employeeDto.name());
        existingEmployee.setEmail(employeeDto.email());
        existingEmployee.setRoles(roleNames);

        if (employeeDto.password() != null && !employeeDto.password().isEmpty()) {
            existingEmployee.setPassword(bCryptPasswordEncoder.encode(employeeDto.password()));
        }

        var updatedEmployee = employeeRepository.save(existingEmployee);

        return toResponseDto(updatedEmployee);
    }

    @Transactional
    public void deleteEmployeeById(UUID email){
        EmployeeModel deleteEmployee = employeeRepository.findById(email)
                .orElseThrow(() -> new NoSuchElementException("Funcionário com email " + email + " não encontrado"));

        if (deleteEmployee.getRoles().stream()
                .anyMatch(role -> role.getName().equals(RoleModel.Values.ADMIN.name()))) {
            throw new IllegalArgumentException("Não é permitido excluir um funcionário com a role ADMIN");
        }

        employeeRepository.delete(deleteEmployee);
    }
}
