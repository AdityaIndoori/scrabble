package com.swe681.scrabble.service;

import com.swe681.scrabble.model.Leaderboard;

import java.util.List;

public interface LeaderboardService {
	List<Leaderboard> findAllUserData();
}
