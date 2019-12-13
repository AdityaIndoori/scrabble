package com.swe681.scrabble.repository;

import com.swe681.scrabble.model.Game;
import com.swe681.scrabble.model.GameStatus;
import com.swe681.scrabble.model.JoinGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinGameRepository extends JpaRepository<JoinGame, Long> {
    List<JoinGame> findByUsername(String username);
}
