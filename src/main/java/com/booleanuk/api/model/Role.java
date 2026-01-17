package com.booleanuk.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Role(ERole name){
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    // Can only take certain values.
    @Column(length = 20)
    // Set the length of the column in the database.
    private ERole name;
}