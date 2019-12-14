package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter //LOMBOK
@Setter //LOMBOK

@Entity //is a JPA annotation which specifies the class as an entity (so the class name can be used in JPQL queries)
@Table(name = "player")
public class Player {
	@Id //declares the identifier property of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Long gameid;
	
	private String username;
	
	private String rack;
	
	private Integer score;
}
