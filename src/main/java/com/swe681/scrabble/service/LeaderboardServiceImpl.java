package com.swe681.scrabble.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swe681.scrabble.model.Leaderboard;
import com.swe681.scrabble.repository.LeaderboardRepository;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {
	
	@Autowired
	private LeaderboardRepository leaderboardRepository;

	@Override
	public List<Leaderboard> findAllUserData() {
		return leaderboardRepository.findAll();
	}

}
