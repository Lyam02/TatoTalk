package com.tatotalk.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

@Entity
@Table(name = "fichier")
public class Fichier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false,  length = 255)
    @NotEmpty
    public String file_name;

    @Column(nullable = true, length = 255)
    public String stockage_url;

    @Column(nullable = false, columnDefinition = "DATETIME")
    public LocalDateTime uploaded_at;

    public int getId() {
        return id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getStockage_url() {
        return stockage_url;
    }

    public void setStockage_url(String stockage_url) {
        this.stockage_url = stockage_url;
    }

    public LocalDateTime getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(LocalDateTime uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

}
