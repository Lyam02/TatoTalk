package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "employes")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false, length = 50)
    @NotEmpty
    public String nom;

    @Column(length = 50, nullable = false)
    @NotEmpty
    public String prenom;

    @Column(length = 150, nullable = false)
    @Email(message = "Email invalide")
    @NotEmpty
    public String email;

    @Column(length = 150, nullable = false)
    @NotEmpty
    public String samaccountname;

    @Column(length = 150, nullable = true)
    public String displayname;

    @Column(length = 150, nullable = true)
    public String department;

    @Column(length = 150, nullable = true)
    public String service;

    @Column(length = 100, nullable = false)
    @NotEmpty
    public String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    public Roles roles;

    public Employees(String nom, String prenom, String email, String samaccountname, String displayname, String department, Roles roles, String service, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.samaccountname = samaccountname;
        this.displayname = displayname;
        this.department = department;
        this.roles = roles;
        this.service = service;
        this.password = password;
    }

    public Employees() {

    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSamaccountname() {
        return samaccountname;
    }

    public void setSamaccountname(String samaccountname) {
        this.samaccountname = samaccountname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
