package com.laborlink.activity.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rented_tool")
@Getter
@Setter
public class RentedTool {

    @Id // Another Id because the user might share the same tool Id (any way tools has quantity)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rId;

    private Long toolId;

    private Integer quantity;

    private Double pricePerSecond;

    @ManyToOne // JPA API, Describe ManyToOne SQL Relation (Many Tools to one activity)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

}
