package com.swe681.scrabble.service;

import java.util.List;

import com.swe681.scrabble.model.GameMove;

public interface GameMoveService {
	List<GameMove> findByGameId(Long gameid) throws Exception;
}
