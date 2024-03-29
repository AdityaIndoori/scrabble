package com.swe681.scrabble.repository;

import com.swe681.scrabble.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	Player findByUsernameAndGameid(String username, Long gameid);

	Player findByUsername(String username);

	List<Player> findByGameid(Long gameid);
}
