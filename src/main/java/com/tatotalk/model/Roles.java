package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;

    @Column(nullable = false, length = 50)
    @NotEmpty
    public String name;

    @Column(columnDefinition = "TEXT")
    public String description;

    public Roles(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Roles() {

    }

    public int getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}