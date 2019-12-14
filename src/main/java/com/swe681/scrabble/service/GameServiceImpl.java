package com.swe681.scrabble.service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                String currentTimeStamp = timestamp.toString();
                game.setP1Username(userName);
                game.setP1TimeStamp(currentTimeStamp);
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
                Date date = new Date();
                Timestamp timestamp = new Timestamp(date.getTime());
                String currentTimeStamp = timestamp.toString();
                List<Game> games = gameRepository.findByStatus(GameStatus.WAIT);
                if(games!=null && games.size()>0){
                    int random_index = getRandomIndex(games.size());
                    Game selectedGame = games.get(random_index);
                    selectedGame.setP2Username(userName);
                    selectedGame.setP2TimeStamp(currentTimeStamp);
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
                boolean playersGame = false;
                String userName = ((UserDetails) principal).getUsername();
                Date date = new Date();
                Timestamp currentTime = new Timestamp(date.getTime());
                //Check if user is playing a game with that game id: STATUS = RUN
                List<Game> runningGameList = gameRepository.findByP1UsernameOrP2UsernameAndStatus(userName, userName, GameStatus.RUN);//todo: START to RUN
                for (Game runningGame : runningGameList){
                    if(runningGame.getId().toString().equals(gameid))
                        playersGame = true;
                    if (playersGame){
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                        Date parsedDate = null;
                        if(userName.equals(runningGame.getP1Username())){
                            parsedDate = dateFormat.parse(runningGame.getP1TimeStamp());
                        }
                        else if(userName.equals(runningGame.getP2Username())){
                            parsedDate = dateFormat.parse(runningGame.getP2TimeStamp());
                        }
                        else {
                            throw new Exception("User not authenticated");
                        }
                        if(parsedDate==null){
                            log.info("getJoinableGames: The date from database was not found for the current user");
                            throw new Exception("Unable to parse date");
                        }

                        Timestamp savedTime = new java.sql.Timestamp(parsedDate.getTime());
                        long differenceInMilliSeconds = currentTime.getTime() - savedTime.getTime();
                        int differenceInSeconds = (int) differenceInMilliSeconds / 1000;
                        if(differenceInSeconds<120)//TODO: set the timeout limit when displaying the table
                            return true;
                    }
                }
                return false;
            } else {
                throw new Exception("Principal not instance of UserDetails");
            }
        } else {
            throw new Exception("User not authenticated");
        }
    }
}
