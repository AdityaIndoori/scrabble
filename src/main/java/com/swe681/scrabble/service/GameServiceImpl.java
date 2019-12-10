package com.swe681.scrabble.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import com.swe681.scrabble.repository.GameRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

/*  TODO: Implement GameLogic class as Service
    @Autowired
    GameLogic gameLogic;*/

    @Autowired
    GameRepository gameRepository;

    @Override
    public Long startGame() throws Exception {
        //Instantiate a Game object: -> Model
        Game game = new Game();

//        gameLogic.initializeBoard(game);//Initializing the rows and columns of the game board

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String userName = ((UserDetails) principal).getUsername();
                game.setPlayer1(userName);
                game.setStatus(GameStatus.WAIT);
                game = gameRepository.save(game);
                return game.getId();
            } else {
                throw new Exception("Principal not instance of UserDetails");
            }
        } else {
            throw new Exception("User not authenticated");
        }
    }

    @Override
    public Long joinGame() throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String userName = ((UserDetails) principal).getUsername();
                List<Game> games = gameRepository.findByStatus(GameStatus.WAIT);
                log.info("Game list size: " + games.size());//TODO: remove log
                if(games!=null && games.size()>0){
                    int random_index = getRandomIndex(games.size());
                    Game selectedGame = games.get(random_index);
                    selectedGame.setPlayer2(userName);
                    selectedGame.setStatus(GameStatus.START);
                    selectedGame = gameRepository.save(selectedGame);
                    return selectedGame.getId();
                }
                else{
                    throw new Exception("No games loaded from database");
                }
            } else {
                throw new Exception("Principal not instance of UserDetails");
            }
        } else {
            throw new Exception("User not authenticated");
        }
    }

    private int getRandomIndex(int size) throws NoSuchProviderException, NoSuchAlgorithmException {
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");

        // Get 128 random bytes
        byte[] randomBytes = new byte[128];
        secureRandomGenerator.nextBytes(randomBytes);

        //Get random integer
        int r = secureRandomGenerator.nextInt();

        return r%size;
    }
    


	@Override
	public List<Game> findByStatus() throws Exception{
		try {
			List<Game> list = gameRepository.findByStatus(GameStatus.FINISHED);
			return list;
		}catch(Exception e) {
			throw e;
		}
	} 
}
