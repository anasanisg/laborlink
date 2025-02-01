package com.laborlink.renting.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "temporary_contract")
@Getter
@NoArgsConstructor

public class Contract {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "r_id")
    private Long rId;

    @Column(name = "user_id",unique = true,nullable = false)
    private String userId;

    @Column(name = "activity_id",unique = true,nullable = false)
    private String activityId;
    
    public Contract(String userId, String activityId){
        this.userId = userId;
        this.activityId = activityId;

    }
}
