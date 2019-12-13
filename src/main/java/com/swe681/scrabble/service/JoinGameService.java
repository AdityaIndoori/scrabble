package com.swe681.scrabble.service;

import com.swe681.scrabble.model.JoinableGame;

import java.util.List;

public interface JoinGameService {
    public void onDisconnect() throws Exception;

    public List<JoinableGame> getJoinableGames() throws Exception;
}
