package com.swe681.scrabble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swe681.scrabble.model.Leaderboard;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long>{
	
	List<Leaderboard> findAll();
}
