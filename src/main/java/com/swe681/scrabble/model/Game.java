package com.swe681.scrabble.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private String p1TimeStamp;

    @Nullable
    private String p2TimeStamp;

    @Nullable
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    @Nullable
    private String bag;
    
    @Nullable
    private String lastMove;
}