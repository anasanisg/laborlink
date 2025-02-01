package com.laborlink.renting.dtos.internal;

import com.laborlink.renting.enums.ToolStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;


@Getter
@Setter
public class Tool extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    
    @Column(name = "name",unique = true,nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "img")
    private String img;

    @Column(name = "price_per_sec",nullable = false)
    private Double pricePerSec;


    @Column(name = "quantity",nullable = false)
    private Integer quantity;
    

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ToolStatus status;




    @PrePersist
    private void nullCheck(){

        if (this.quantity == null)
        this.quantity =1;


        if(this.status == null)
        this.status = ToolStatus.NOT_SET; 
    }




}
