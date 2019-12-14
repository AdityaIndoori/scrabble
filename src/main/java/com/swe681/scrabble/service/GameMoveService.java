package com.swe681.scrabble.service;

import com.swe681.scrabble.model.GameMove;

import java.util.List;

public interface GameMoveService {
	List<GameMove> findByGameId(Long gameid) throws Exception;
}
