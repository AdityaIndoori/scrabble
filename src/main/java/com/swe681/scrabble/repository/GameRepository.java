package com.swe681.scrabble.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByP1UsernameOrP2UsernameAndStatus(String p1Username, String p2Username, GameStatus status);

    List<Game> findByStatus(GameStatus status);
    
    Game save(Game game);
    
    //Optional<Game> findById(Long id);
    
//    Game findById(Long id);
}
