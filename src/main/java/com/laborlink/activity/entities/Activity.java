package com.laborlink.activity.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.laborlink.activity.enums.ActivityStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "activity")
@Getter
@Setter
public class Activity extends BaseModel {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    @Column(name = "rented_tools", nullable = true)
    private List<RentedTool> rentedTools;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "activity_id")
    @Column(name = "returned_tools", nullable = true)
    private List<ReturnedTool> returnedTools;

    @Column(name = "rented_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMMM yyyy HH:mm:ss")
    private LocalDateTime rentedAt;

    @Column(name = "total_price")
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActivityStatus status;



    @PrePersist
    public void defineNulls(){
        this.totalPrice = 0.0;
    }

}
