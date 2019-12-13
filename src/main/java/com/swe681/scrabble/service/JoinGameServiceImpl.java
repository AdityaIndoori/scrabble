package com.swe681.scrabble.service;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import com.swe681.scrabble.model.JoinableGame;
import com.swe681.scrabble.repository.GameRepository;
import com.swe681.scrabble.repository.JoinGameRepository;
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
    JoinGameRepository joinGameRepository;

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
                    httpSession.setAttribute("gameid", null);
                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    String timestampStr = timestamp.toString();
                    JoinableGame joinableGame = new JoinableGame(userName, gameid, timestampStr);
                    joinGameRepository.save(joinableGame);
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
                    List<JoinableGame> joinableGameList = joinGameRepository.findByUsername(userName);
                    List<JoinableGame> joinableList = new ArrayList<>();
                    List<Game> runningGamesList = gameRepository.findByP1UsernameOrP2UsernameAndStatus(userName, userName, GameStatus.START);//todo: LOGIC: Game was in Start but we are searching for Run
                    List<JoinableGame> outputList = new ArrayList<>();
                    for(JoinableGame joinableGame : joinableGameList){
                        log.info(String.format("Username is %s, Gameid is %s, and game was diconnected at %s", userName, joinableGame.getGameid(), joinableGame.getTimestamp()));
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                            Date parsedDate = dateFormat.parse(joinableGame.getTimestamp());
                            Timestamp savedTime = new java.sql.Timestamp(parsedDate.getTime());
                            long milliseconds = currentTime.getTime() - savedTime.getTime();
                            int differenceInSeconds = (int) milliseconds / 1000;
                            log.info(String.format("TRY: Username is %s, Gameid is %s, Game was left at %s, current time is %s, and difference was %s", userName, savedTime.toString(), joinableGame.getTimestamp(), currentTime.toString(), differenceInSeconds));
                            if(differenceInSeconds<120) //TODO: set the timeout limit when displaying the table
                                joinableList.add(joinableGame);
                        } catch(Exception e) { //this generic but you can control another types of exception
                            throw new Exception("Could not parse timestamp from string");
                        }
                    }
                    log.info(String.format("Joinable Games Length = %s",joinableList.size()));
                    log.info(String.format("RUNNING Games Length = %s",runningGamesList.size()));

                    for (Game game : runningGamesList){
                        for(JoinableGame game1 : joinableList){
                            log.info(String.format("Joinable GameID = %s and Running GAMEID = %s",game1.getGameid(), game.getId().toString()));
                            if(String.valueOf(game.getId()).equals(game1.getGameid()) && !outputList.contains(game1)) {
                                log.info(String.format("IF TRUE: Joinable GameID = %s and Running GAMEID = %s",game1.getGameid(), game.getId().toString()));
                                outputList.add(game1);
                            }
                        }
                    }
                    log.info(String.format("Output Games Length = %s",outputList.size()));
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
