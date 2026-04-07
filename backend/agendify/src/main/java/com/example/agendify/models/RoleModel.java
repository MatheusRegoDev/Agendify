package com.example.agendify.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "TB_ROLES")
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long idRole;

    private String name;

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Values {

        ADMIN(1L),
        RECEPTIONIST(2L),
        ATTENDANT_G1(3L),
        ATTENDANT_G2(4L);

        long idRole;

        Values(long idRole) {
            this.idRole = idRole;
        }
    }
}
