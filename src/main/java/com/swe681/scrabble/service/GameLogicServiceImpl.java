package com.swe681.scrabble.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import com.swe681.scrabble.model.Player;
import com.swe681.scrabble.repository.GameRepository;
import com.swe681.scrabble.repository.PlayerRepository;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class GameLogicServiceImpl implements GameLogicService {
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	PlayerRepository playerRepository;
	
	static final Map<String, Integer> LETTER_SCORE;
	
	static {
		LETTER_SCORE = new HashMap<>();
		LETTER_SCORE.put("A", 1);
		LETTER_SCORE.put("B", 3);
		LETTER_SCORE.put("C", 3);
        LETTER_SCORE.put("D", 2);
        LETTER_SCORE.put("E", 1);
		LETTER_SCORE.put("F", 4);
		LETTER_SCORE.put("G", 2);
        LETTER_SCORE.put("H", 4);
        LETTER_SCORE.put("I", 1);
		LETTER_SCORE.put("J", 1);
		LETTER_SCORE.put("K", 5);
        LETTER_SCORE.put("L", 1);
        LETTER_SCORE.put("M", 3);
		LETTER_SCORE.put("N", 1);
		LETTER_SCORE.put("O", 1);
        LETTER_SCORE.put("P", 3);
        LETTER_SCORE.put("Q", 10);
        LETTER_SCORE.put("R", 1);
        LETTER_SCORE.put("S", 1);
		LETTER_SCORE.put("T", 1);
		LETTER_SCORE.put("U", 1);
        LETTER_SCORE.put("V", 4);
        LETTER_SCORE.put("W", 4);
		LETTER_SCORE.put("X", 8);
		LETTER_SCORE.put("Y", 4);
        LETTER_SCORE.put("Z", 10);
    }
	
	@Override
	public void createGame(Long gameid) throws Exception{
		try {
			Game game = gameRepository.findById(gameid).get();
			if(game!=null) {
				if(game.getP1Username()!=null && game.getP2Username()!=null) {
					
					initializeBag(gameid);
					
					initializeBoard(gameid);
					
					initializePlayer(game.getP1Username(), gameid);
					initializePlayer(game.getP2Username(), gameid);
					fillPlayerRack(game.getP1Username(), gameid);
					fillPlayerRack(game.getP2Username(), gameid);
				}
				else {
					gameRepository.save(game);
				}
			}
		}catch(Exception ex) {
			throw ex;
		}
	}
	
	@Override
	public void initializeBag(Long gameid) throws Exception{
		try {
			String bag = "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGGHHIIIIIIIIIJJJJJJJJJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
			String shuffledBag = shuffle(bag);
			
			Game game = gameRepository.findById(gameid).get();
			
			//log.info("In GameLogicService------- game before save bag:"+game.toString());
			
			if(game!=null) {
				game.setBag(shuffledBag);
				game.setStatus(GameStatus.RUN);
				game = gameRepository.save(game);
			}
		}catch(Exception ex){
			throw ex;
		}
		
	}
	
	@Override
	public void initializeBoard(Long gameid) throws Exception{
		// TODO Auto-generated method stub
		try {
			Game game = gameRepository.findById(gameid).get();
			
			//log.info("In GameLogicService------- game before save board:"+game.toString());
			
			if(game!=null) {
				StringBuilder localBoard = new StringBuilder();
				for(int i=0; i<5; i++) {
					StringBuilder row = new StringBuilder();
					for(int j=0; j<5; j++) {
						row.append(" ");
					}
					localBoard.append(row.toString());
				}
				
				game.setBoard(localBoard.toString());
				
				game = gameRepository.save(game);
				
			}
		}catch(Exception ex){
			throw ex;
		}
		
	}
	
	
	private void fillPlayerRack(String username, Long gameid) throws Exception{
		try {
			Player player = playerRepository.findByUsernameAndGameid(username, gameid);
			Game game = gameRepository.findById(gameid).get();
			
			//log.info("In GameLogicService------- game before setting rack:"+game.toString());
			
			
			if(player!=null && game!=null && (player.getRack().length()<7)) {
				StringBuilder localRack = new StringBuilder();
				localRack.append(player.getRack());
				for(int i=0; i<(7 - player.getRack().length()); i++) {
					localRack.append(game.getBag().substring(0, 1));
					game.setBag(game.getBag().substring(1));
				}
				
				player.setRack(localRack.toString());
				
				gameRepository.save(game);
				playerRepository.save(player);
			}
		}catch(Exception ex){
			throw ex;
		}
	}
	
	private void initializePlayer(String username, Long gameid) throws Exception{
		try {
			Player player = new Player();
			player.setGameid(gameid);
			player.setUsername(username);
			player.setRack("");
			player.setScore(0);
			playerRepository.save(player);
		}catch(Exception ex){
			throw ex;
		}
	}
	
	
	private String shuffle(String input){
		
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
		return output.toString();
    }

}
