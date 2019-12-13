package com.swe681.scrabble.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swe681.scrabble.model.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	Player findByUsernameAndGameid(String username, Long gameid);

	Player findByUsername(String username);
}
