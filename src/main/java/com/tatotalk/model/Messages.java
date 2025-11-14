package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "messages")
public class Messages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotEmpty
    public String message_content;

    @Column(nullable = true, length = 50)
    public String type;

    @Column(nullable = true, columnDefinition = "DATETIME")
    public LocalDateTime edited_at;

    @Column(nullable = false, columnDefinition = "DATETIME")
    public LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "conv_id")
    public Conversations conversations;

    @ManyToOne
    @JoinColumn(name = "sendBy")
    public Employees sendBy;

    @ManyToOne
    @JoinColumn(name = "sendTo")
    public Employees sendTo;

    public Messages(int id, String message_content, String type, LocalDateTime edited_at, LocalDateTime created_at, Conversations conversations, Employees sendBy, Employees sendTo) {
        this.id = id;
        this.message_content = message_content;
        this.type = type;
        this.edited_at = edited_at;
        this.created_at = created_at;
        this.conversations = conversations;
        this.sendBy = sendBy;
        this.sendTo = sendTo;
    }

    public Messages() {

    }

    public int getId() {
        return id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public Employees getSendBy() {
        return sendBy;
    }

    public void setSendBy(Employees sendBy) {
        this.sendBy = sendBy;
    }

    public Employees getSendTo() {
        return sendTo;
    }

    public void setSendTo(Employees sendTo) {
        this.sendTo = sendTo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getEdited_at() {
        return edited_at;
    }

    public void setEdited_at(LocalDateTime edited_at) {
        this.edited_at = edited_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Conversations getConversations() {
        return conversations;
    }

    public void setConversations(Conversations conversations) {
        this.conversations = conversations;
    }
}