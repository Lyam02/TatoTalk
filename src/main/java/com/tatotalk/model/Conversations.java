package com.tatotalk.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false, columnDefinition = "DATETIME")
    public LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "created_by")
    public Employees created_by;

    public Conversations(int id, LocalDateTime created_at, Employees created_by) {
        this.id = id;
        this.created_at = created_at;
        this.created_by = created_by;
    }

    public Conversations() {

    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Employees getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Employees created_by) {
        this.created_by = created_by;
    }
}
