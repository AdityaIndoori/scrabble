package com.swe681.scrabble.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.swe681.scrabble.model.JoinGame;
import com.swe681.scrabble.repository.JoinGameRepository;
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

    @Autowired
    JoinGameRepository joinGameRepository;

    @Override
    public Long startGame() throws Exception {
        //Instantiate a Game object: -> Model
        Game game = new Game();

//        gameLogic.initializeBoard(game);//Initializing the rows and columns of the game board

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String userName = ((UserDetails) principal).getUsername();
                game.setP1Username(userName);
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
                if(games!=null && games.size()>0){
                    int random_index = getRandomIndex(games.size());
                    Game selectedGame = games.get(random_index);
                    selectedGame.setP2Username(userName);
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

    @Override
    public boolean rejoinGame(String gameid) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                String userName = ((UserDetails) principal).getUsername();
                Date date = new Date();
                Timestamp currentTime = new Timestamp(date.getTime());
                //Check if user is playing a game with that game id: STATUS = RUN
                List<Game> games = gameRepository.findByP1UsernameOrP2UsernameAndStatus(userName, userName, GameStatus.START);//todo: START to RUN
                //Check if the game has a difference of less than 120 seconds
                List<JoinGame> joinGameList = joinGameRepository.findByUsername(userName);
                //Add such games to this joinableList
                List<JoinGame> joinableList = new ArrayList<>();
                for(JoinGame joinGame : joinGameList){
                    log.info(String.format("Username is %s, Gameid is %s, and game was joined at %s", userName, joinGame.getGameid(), joinGame.getTimestamp()));
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                        Date parsedDate = dateFormat.parse(joinGame.getTimestamp());
                        Timestamp savedTime = new java.sql.Timestamp(parsedDate.getTime());
                        long milliseconds = currentTime.getTime() - savedTime.getTime();
                        int differenceInSeconds = (int) milliseconds / 1000;
                        log.info(String.format("TRY: Username is %s, Gameid is %s, Game was left at %s, current time is %s, and difference was %s", userName, savedTime.toString(), joinGame.getTimestamp(), currentTime.toString(), differenceInSeconds));
                        if(differenceInSeconds<120)//TODO: set the timeout limit when joining game
                            joinableList.add(joinGame);
                    } catch(Exception e) { //this generic but you can control another types of exception
                        throw new Exception("Could not parse timestamp from string");
                    }
                }
                //If the game id is of correct user and game id cis within the timeoutlimit, only then return true
                boolean correctUser = false;
                boolean correctTime = false;
                for(Game game : games){
                    if(game.getId().toString().equals(gameid))
                        correctUser = true;
                }
                if (!correctUser)
                    return false;
                for (JoinGame joinGame : joinableList){
                    if(joinGame.getGameid().equals(gameid))
                        correctTime = true;
                }
                if(!correctTime)
                    return false;
                return correctUser & correctTime;
            } else {
                throw new Exception("Principal not instance of UserDetails");
            }
        } else {
            throw new Exception("User not authenticated");
        }
    }
}
