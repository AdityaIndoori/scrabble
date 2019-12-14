package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor //LOMBOK
@Entity
@Table(name="leaderboard")
public class Leaderboard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String username;

    private Integer won;

    private Integer lost;

    private Integer total;
}