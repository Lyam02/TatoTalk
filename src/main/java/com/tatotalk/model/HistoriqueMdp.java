package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_mdp")
public class HistoriqueMdp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = true, columnDefinition = "DATETIME")
    public LocalDateTime date_historique;

    @Column(length = 100, nullable = true)
    @NotEmpty
    public String old_password;

    @ManyToOne
    @JoinColumn(name = "id_Employee")
    public Employees Id_Employee;

    public int getId() {
        return id;
    }

    public LocalDateTime getDate_historique() {
        return date_historique;
    }

    public void setDate_historique(LocalDateTime date_historique) {
        this.date_historique = date_historique;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public Employees getId_Employee() {
        return Id_Employee;
    }

    public void setId_Employee(Employees id_Employee) {
        Id_Employee = id_Employee;
    }
}
