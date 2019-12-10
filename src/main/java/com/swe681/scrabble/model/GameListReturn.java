package com.swe681.scrabble.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameListReturn {
	
	private Long id;

    private String player1;

    private String player2;

    @Enumerated(EnumType.STRING)
    private GameStatus status;
}
