package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor //LOMBOK
@Entity //is a JPA annotation which specifies the class as an entity (so the class name can be used in JPQL queries)
@Table(name = "game")
public class Game {
    @Id //declares the identifier property of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String row1;

    @Nullable
    private String row2;

    @Nullable
    private String row3;

    @Nullable
    private String row4;

    @Nullable
    private String row5;

    @Nullable
    private String row6;

    @Nullable
    private String row7;

    @Nullable
    private String row8;

    @Nullable
    private String row9;

    @Nullable
    private String row10;

    @Nullable
    private String player1;

    @Nullable
    private String player2;

    @Nullable
    private GameStatus status;
}