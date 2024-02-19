package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "relations")
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(mappedBy = "relations")
    private Set<User> users = new HashSet<>();

    public Relation(User user1, User user2) {
        this.users.add(user1);
        this.users.add(user2);
        user1.getRelations().add(this);
        user2.getRelations().add(this);
    }
}
