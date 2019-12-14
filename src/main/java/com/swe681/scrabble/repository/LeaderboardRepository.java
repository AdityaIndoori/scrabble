package com.swe681.scrabble.repository;

import com.swe681.scrabble.model.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long>{
	
	List<Leaderboard> findAll();
}
