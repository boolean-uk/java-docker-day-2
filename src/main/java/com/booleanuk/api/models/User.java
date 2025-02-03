package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column
    private String userName;

    @Column
    private String creationDate;

    @Column
    private int numberOfPosts;

    @Column
    private int numberOfFollowers;




    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value ={"posts", "followers", "users"})
    private List<Post> posts;



    public User(String userName, String creationDate){
        this.userName=userName;
        this.creationDate=creationDate;
        this.numberOfFollowers=0;
        this.numberOfPosts=0;
    }

}
