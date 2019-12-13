package com.swe681.scrabble.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameListReturn;
import com.swe681.scrabble.model.GameMove;
import com.swe681.scrabble.model.Leaderboard;
import com.swe681.scrabble.service.GameMoveService;
import com.swe681.scrabble.service.GameService;
import com.swe681.scrabble.service.LeaderboardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GameRestController {
	
	@Autowired
	LeaderboardService leaderboardService;
	
	@Autowired
	GameService gameService;
	
	@Autowired
	GameMoveService gameMoveService;
	
	@GetMapping(value = "/leaderboard", produces = "application/json")
    public ResponseEntity<List<Leaderboard>> getLeaderboard() {
		try {
			if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
	            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            if (principal instanceof UserDetails) {
		        	List<Leaderboard> list = leaderboardService.findAllUserData();
		            return new ResponseEntity<>(list, HttpStatus.OK);
	            }
	        }
			return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED); 
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
	
	
	@GetMapping(value = "/gamelist", produces = "application/json")
    public ResponseEntity<List<GameListReturn>> getGameList() {
		try {
			if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
	            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            if (principal instanceof UserDetails) {
		        	List<Game> list = gameService.findByStatus();
		        	List<GameListReturn> returningList = new ArrayList<GameListReturn>();
		        	for(Game g: list) {
		        		GameListReturn glr = new GameListReturn();
		        		glr.setId(g.getId());
		        		glr.setPlayer1(g.getP1Username());
		        		glr.setPlayer2(g.getP2Username());
		        		glr.setStatus(g.getStatus());
		        		returningList.add(glr);
		        		log.info("In gamelist API ----- GET ID ------------------------------"+glr.getId());
		        	}
		        	
		            return new ResponseEntity<>(returningList, HttpStatus.OK);
	            }
	        }
			return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED); 
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
	
	
	@GetMapping(value = "/gamemoves/{gameid}", produces = "application/json")
    public ResponseEntity<List<GameMove>> getGameMoves(@PathVariable String gameid) {
		try {
			if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
	            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            if (principal instanceof UserDetails) {
		        	List<GameMove> list = gameMoveService.findByGameId(Long.parseLong(gameid));
		            return new ResponseEntity<>(list, HttpStatus.OK);
	            }
	        }
			return new ResponseEntity<>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED); 
		}catch(Exception e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
	
}
