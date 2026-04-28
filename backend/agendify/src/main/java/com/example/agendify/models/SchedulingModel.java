package com.example.agendify.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "TB_SCHEDULINGS")
public class SchedulingModel  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "scheduling_id")
    private UUID idScheduling;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientModel client;

    @Column(nullable = false)
    private LocalDate schedulingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;


    public UUID getIdScheduling() {
        return idScheduling;
    }

    public void setIdScheduling(UUID idScheduling) {
        this.idScheduling = idScheduling;
    }

    public ClientModel getClient() {
        return client;
    }

    public void setClient(ClientModel client) {
        this.client = client;
    }

    public LocalDate getSchedulingDate() {
        return schedulingDate;
    }

    public void setSchedulingDate(LocalDate schedulingDate) {
        this.schedulingDate = schedulingDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        SCHEDULED,
        COMPLETED,
        CONFIRMED,
        CANCELED,
        RESCHEDULED
    }
}
