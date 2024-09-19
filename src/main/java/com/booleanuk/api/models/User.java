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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String username;


	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER) // map til message sin variabel "user"
	@JsonIgnoreProperties(value = {"messages"}) // ignorer user sine messages.
	@JsonIgnore
	private List<Message> messages;
}
