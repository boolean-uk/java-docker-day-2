package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data

@Entity
@Table(name="users")
public class User {
	public User(Integer id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String username;


	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // map til message sin variabel "user"
	@JsonIgnoreProperties(value = {"messages"}) // ignorer user sine messages.
	@JsonIgnore
	private List<Message> messages;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonIgnoreProperties("user")
	@JsonIgnore
	private List<Like> likes;

}
