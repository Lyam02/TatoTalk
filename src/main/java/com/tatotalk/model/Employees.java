package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "employes")
public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;

    @Column(nullable = false, length = 50)
    @NotEmpty
    public String nom;

    @Column(length = 50, nullable = false)
    @NotEmpty
    public String prenom;

    @Column(length = 150, nullable = false)
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


}
