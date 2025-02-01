package com.laborlink.tool.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseModel {


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;


    @PrePersist
    private void onCreated(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onModified(){
        this.modifiedAt = LocalDateTime.now();
    }

}

