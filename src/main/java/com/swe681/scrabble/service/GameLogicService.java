package com.swe681.scrabble.service;

import com.swe681.scrabble.model.MoveWS;
import com.swe681.scrabble.model.OutputMove;

public interface GameLogicService {

	void createGame(Long gameid) throws Exception;

	String playMove(MoveWS move) throws Exception;

	OutputMove settingOutputMove(MoveWS move) throws Exception;

	String showGameRack(String gameid, String username) throws Exception;
}
