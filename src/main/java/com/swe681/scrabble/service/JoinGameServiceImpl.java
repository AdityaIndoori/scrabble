package com.swe681.scrabble.service;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import com.swe681.scrabble.model.JoinableGame;
import com.swe681.scrabble.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JoinGameServiceImpl implements JoinGameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    HttpSession httpSession;

    @Override
    public void onDisconnect() throws Exception {
        try {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    String userName = ((UserDetails) principal).getUsername();
                    String gameid = httpSession.getAttribute("gameid").toString();
                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    String currentTimeStamp = timestamp.toString();
                    //Get current game by Id:
                    Game currentGame = gameRepository.findById(Long.parseLong(gameid)).get();
                    //Check if player1 or player 2:
                    if(userName.equals(currentGame.getP1Username()) ){
                        currentGame.setP1TimeStamp(currentTimeStamp);
                    }
                    else if(userName.equals(currentGame.getP2Username()) ){
                        currentGame.setP2TimeStamp(currentTimeStamp);
                    }
                    else {
                        throw new Exception("User not authenticated");
                    }
                    gameRepository.save(currentGame);
                } else {
                    throw new Exception("Principal not instance of UserDetails");
                }
            } else {
                throw new Exception("User not authenticated");
            }
        }
        catch (Exception e){
            throw new Exception("Could not save data to joingame");
        }
    }

    @Override
    public void onMoveSubmit(String gameid, String userName) throws Exception {
        try {
            if (true) {
                if (true) {
                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    String currentTimeStamp = timestamp.toString();
                    //Get current game by Id:
                    Game currentGame = gameRepository.findById(Long.parseLong(gameid)).get();
                    //Check if player1 or player 2:
                    if(userName.equals(currentGame.getP1Username()) ){
                        currentGame.setP1TimeStamp(currentTimeStamp);
                    }
                    else if(userName.equals(currentGame.getP2Username()) ){
                        currentGame.setP2TimeStamp(currentTimeStamp);
                    }
                    else {
                        throw new Exception("User not authenticated");
                    }
                    gameRepository.save(currentGame);
                } else {
                    throw new Exception("Principal not instance of UserDetails");
                }
            } else {
                throw new Exception("User not authenticated");
            }
        }
        catch (Exception e){
            throw new Exception("Could not save data to joingame");
        }
    }

    @Override
    public List<JoinableGame> getJoinableGames() throws Exception {
        try {
            if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof UserDetails) {
                    String userName = ((UserDetails) principal).getUsername();
                    Date date = new Date();
                    Timestamp currentTime = new Timestamp(date.getTime());
                    //GET List of games by username from join game list

                    List<Game> runningGamesList = gameRepository.findByP1UsernameOrP2UsernameAndStatus(userName, userName, GameStatus.RUN);//todo: LOGIC: Game was in Start but we are searching for Run
                    List<JoinableGame> outputList = new ArrayList<>();
                    for(Game runningGame: runningGamesList){
                        try {
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
                            if(differenceInSeconds<120) //TODO: set the timeout limit when displaying the table
                                outputList.add(new JoinableGame(userName, runningGame.getId().toString(), parsedDate.toString()));
                        } catch(Exception e) { //this generic but you can control with other types of exception
                            throw new Exception("Could not parse timestamp from string");
                        }
                    }
                    return outputList;
                } else {
                    throw new Exception("Principal not instance of UserDetails");
                }
            } else {
                throw new Exception("User not authenticated");
            }
        }
        catch (Exception e){
            throw new Exception("Could not save data to joingame");
        }
    }
}
