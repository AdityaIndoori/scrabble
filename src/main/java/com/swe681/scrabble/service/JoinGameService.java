package com.swe681.scrabble.service;

import com.swe681.scrabble.model.JoinableGame;

import java.util.List;

public interface JoinGameService {
    public void onMoveOrDisconnect() throws Exception;

    public void onMoveOrDisconnect(String gameid, String userName) throws Exception;


    public List<JoinableGame> getJoinableGames() throws Exception;
}
