package com.booleanuk.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String content;
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonIgnore
	private User user;
}
