package com.swe681.scrabble.repository;

import com.swe681.scrabble.model.JoinableGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinGameRepository extends JpaRepository<JoinableGame, Long> {
    List<JoinableGame> findByUsername(String username);
}
