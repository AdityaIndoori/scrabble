package com.swe681.scrabble.repository;

import com.swe681.scrabble.model.GameMove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameMoveRepository extends JpaRepository<GameMove, Long> {
	List<GameMove> findByGameid(Long gameid);
	
}
