package com.swe681.scrabble.service;

public interface GameLogicService {
    void initializeBoard(Long gameid) throws Exception; //TODO: Implement this logic

	void initializeBag(Long gameid) throws Exception;

	void createGame(Long gameid) throws Exception;

}