package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "password_token")
public class PasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = true, length = 50)
    @NotEmpty
    public String token;

    @Column(nullable = true, columnDefinition = "DATETIME")
    public LocalDateTime date_reset_expiration;

    @OneToOne
    @JoinColumn(name = "id_Employee")
    public Employees Id_Employee;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDate_reset_expiration() {
        return date_reset_expiration;
    }

    public void setDate_reset_expiration(LocalDateTime date_reset_expiration) {
        this.date_reset_expiration = date_reset_expiration;
    }

    public Employees getId_Employee() {
        return Id_Employee;
    }

    public void setId_Employee(Employees id_Employee) {
        Id_Employee = id_Employee;
    }
}
