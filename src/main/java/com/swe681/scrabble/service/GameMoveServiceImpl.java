package com.swe681.scrabble.service;

import java.util.ArrayList;
import java.util.List;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import com.swe681.scrabble.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swe681.scrabble.model.GameMove;
import com.swe681.scrabble.repository.GameMoveRepository;

@Service
public class GameMoveServiceImpl implements GameMoveService{
	
	@Autowired
	private GameMoveRepository gameMoveRepository;

	@Autowired
	private GameRepository gameRepository;

	@Override
	public List<GameMove> findByGameId(Long gameid) throws Exception{
		try {
			List<GameMove> listOfGameMoves = gameMoveRepository.findByGameid(gameid);
			List<GameMove> outputListOfGameMoves = new ArrayList<>();
			List<Game> allgamesList = gameRepository.findAll();
			for (GameMove currentMove : listOfGameMoves){
				String currentGameId= currentMove.getGameid().toString();
				for (Game game : allgamesList){
					if (game.getId().toString().equals(currentGameId) && game.getStatus().equals(GameStatus.FINISHED)){
						outputListOfGameMoves.add(currentMove);
					}
				}
			}
			return outputListOfGameMoves;
		}catch(Exception e) {
			throw e;
		}
	}
}
