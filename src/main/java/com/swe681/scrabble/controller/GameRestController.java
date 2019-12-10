package com.swe681.scrabble.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swe681.scrabble.model.Leaderboard;
import com.swe681.scrabble.service.LeaderboardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GameRestController {
	
	@Autowired
	LeaderboardService leaderboardService;
	
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
	
}
