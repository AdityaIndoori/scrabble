package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class GameListReturn {
	
	private Long id;

    private String player1;

    private String player2;

    @Enumerated(EnumType.STRING)
    private GameStatus status;
}
