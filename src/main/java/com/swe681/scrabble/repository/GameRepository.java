package com.swe681.scrabble.repository;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByP1UsernameOrP2UsernameAndStatus(String p1Username, String p2Username, GameStatus status);

    List<Game> findByStatus(GameStatus status);

    //Optional<Game> findById(Long id);
    
}
