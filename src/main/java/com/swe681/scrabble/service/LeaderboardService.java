package com.swe681.scrabble.service;

import java.util.List;

import com.swe681.scrabble.model.Leaderboard;

public interface LeaderboardService {
	List<Leaderboard> findAllUserData();
}
