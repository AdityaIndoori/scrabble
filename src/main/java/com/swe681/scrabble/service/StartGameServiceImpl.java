package com.swe681.scrabble.service;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import com.swe681.scrabble.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {

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
}
