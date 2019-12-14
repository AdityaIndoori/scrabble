package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

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
