package com.example.agendify.config;

import com.example.agendify.models.EmployeeModel;
import com.example.agendify.models.RoleModel;
import com.example.agendify.repositories.EmployeeRepository;
import com.example.agendify.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
public class AdminConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminConfig(RoleRepository roleRepository,
                       EmployeeRepository employeeRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(RoleModel.Values.ADMIN.name());
        var employeeAdmin = employeeRepository.findByEmail("admin@agendify.net");

        employeeAdmin.ifPresentOrElse(
                employeeModel -> {
                    System.out.println("Admin já existe, pulando criação...");
                    },

                () -> {
                    if (roleAdmin.isPresent()) {
                        var employee = new EmployeeModel();
                        employee.setName("Admin");
                        employee.setEmail("admin@agendify.net");
                        employee.setPassword(bCryptPasswordEncoder.encode("admin123"));
                        employee.setRoles(Set.of(roleAdmin.get()));
                        employeeRepository.save(employee);
                    }
                }
        );
    }
}
