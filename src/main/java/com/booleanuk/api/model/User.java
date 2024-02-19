package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

//    @Column
//    private LocalDateTime createdAt;
//    @Column
//    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user")
    @JsonIncludeProperties(value = {"id","name", "email", "phone"})
    private List<Post> posts;

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
    }

    public User(Integer id) {
        this.id = id;
    }

//    public void updateUser() {
//        this.updatedAt = LocalDateTime.now();
//    }

}
