package com.swe681.scrabble.service;

import com.swe681.scrabble.model.Leaderboard;
import com.swe681.scrabble.repository.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {
	
	@Autowired
	private LeaderboardRepository leaderboardRepository;

	@Override
	public List<Leaderboard> findAllUserData() {
		return leaderboardRepository.findAll();
	}

}
