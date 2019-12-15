package com.swe681.scrabble.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor //LOMBOK
@Entity
@Table(name="moves")
public class GameMove {
	@Id //declares the identifier property of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Nullable
    private Long gameid;
	
	@Nullable
	private String username;
	
	@Nullable
	private String word;
	
	@Nullable
	private String boardrow;
	
	@Nullable
	private String boardcolumn;
	
	@Nullable
    @Enumerated(EnumType.STRING)
    private Direction direction;
}
