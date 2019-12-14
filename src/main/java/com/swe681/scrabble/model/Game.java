package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.lang.Nullable;

import javax.persistence.*;

@Getter //LOMBOK
@Setter //LOMBOK
@ToString //LOMBOK
@NoArgsConstructor //LOMBOK
@Entity //is a JPA annotation which specifies the class as an entity (so the class name can be used in JPQL queries)
@Table(name = "game")
public class Game {
    @Id //declares the identifier property of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String board;

    @Nullable
    private String p1Username;

    @Nullable
    private String p2Username;

    @Nullable
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    @Nullable
    private String bag;
    
    @Nullable
    private String lastMove;
}