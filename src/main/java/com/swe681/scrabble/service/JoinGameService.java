package com.swe681.scrabble.service;

import com.swe681.scrabble.model.JoinableGame;

import java.util.List;

public interface JoinGameService {
    void onDisconnect() throws Exception;

    void onMoveSubmit(String gameid, String userName) throws Exception;


    List<JoinableGame> getJoinableGames() throws Exception;
}
