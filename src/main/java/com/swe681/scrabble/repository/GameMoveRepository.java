package com.swe681.scrabble.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swe681.scrabble.model.GameMove;

public interface GameMoveRepository extends JpaRepository<GameMove, Long> {
	List<GameMove> findByGameid(Long gameid);
}
