package com.swe681.scrabble.service;

import com.swe681.scrabble.model.Game;

public interface GameService {
    Long startGame() throws Exception;

    Long joinGame() throws Exception;
}
