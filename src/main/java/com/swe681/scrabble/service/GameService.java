package com.swe681.scrabble.service;

import java.util.List;

import com.swe681.scrabble.model.Game;

public interface GameService {
    Long startGame() throws Exception;

    Long joinGame() throws Exception;
    
    List<Game> findByStatus() throws Exception;

    boolean rejoinGame(String gameid) throws Exception;
}
