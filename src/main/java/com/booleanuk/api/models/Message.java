package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name="messages")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String postedDate;

	@Column
	private String text;

	@ManyToOne
	@JoinColumn(name="userID") // navn i kolonnen
	@JsonIgnoreProperties(value = {"messages"}) // ignorer denne verdien for å unngå recur
	private User user;

}
