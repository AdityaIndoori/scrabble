package com.swe681.scrabble.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swe681.scrabble.model.GameMove;
import com.swe681.scrabble.repository.GameMoveRepository;

@Service
public class GameMoveServiceImpl implements GameMoveService{
	
	@Autowired
	private GameMoveRepository gameMoveRepository;

	@Override
	public List<GameMove> findByGameId(Long gameid) throws Exception{
		try {
			List<GameMove> list = gameMoveRepository.findByGameid(gameid);
			return list;
		}catch(Exception e) {
			throw e;
		}
	}

}
