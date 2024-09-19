package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ManyToOne
    @JoinColumn(name = "userOneId", nullable = false)
    private User u1;

    @ManyToOne
    @JoinColumn(name = "userTwoId", nullable = false)
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