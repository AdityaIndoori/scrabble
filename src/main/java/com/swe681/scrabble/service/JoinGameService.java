package com.swe681.scrabble.service;

import com.swe681.scrabble.model.JoinGame;

import java.util.List;

public interface JoinGameService {
    public void saveToDatabase() throws Exception;

    public List<JoinGame> timeOut() throws Exception;
}
