package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "friends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userOneId", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User u1;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userTwoId", nullable = false)
    @JsonIgnoreProperties({"aboutMe", "posts", "createdAt"})
    private User u2;

    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yy-HH:mm:ss")
    private LocalDateTime createdAt;


    public Friend(User u1, User u2) {
        this.u1 = u1;
        this.u2 = u2;
        this.createdAt = LocalDateTime.now();
    }
}