package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserSession")
public class UserSession {

    @Id
    @Column(nullable = false, length = 200)
    @NotEmpty
    public String sessionId;

    @Column(nullable = false, columnDefinition = "DATETIME")
    public LocalDateTime dateConnexion;

    @OneToOne
    @JoinColumn(nullable = false, name = "employee_id")
    public Employees employees;

    public UserSession(String sessionId, LocalDateTime dateConnexion, Employees employees) {
        this.sessionId = sessionId;
        this.dateConnexion = dateConnexion;
        this.employees = employees;
    }

    public UserSession() {

    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getDateConnexion() {
        return dateConnexion;
    }

    public void setDateConnexion(LocalDateTime dateConnexion) {
        this.dateConnexion = dateConnexion;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }
}
